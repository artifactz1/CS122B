package com.github.klefstad_teaching.cs122b.gateway.filter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.github.klefstad_teaching.cs122b.gateway.config.GatewayServiceConfig;
import com.github.klefstad_teaching.cs122b.gateway.repo.GatewayRepo;
import com.github.klefstad_teaching.cs122b.gateway.request.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalLoggingFilter.class);
    private static final Scheduler DB_SCHEDULER = Schedulers.boundedElastic();

    private final GatewayRepo gatewayRepo;
    private final GatewayServiceConfig config;
    private final LinkedBlockingQueue<Request> requests = new LinkedBlockingQueue<>();

    @Autowired
    public GlobalLoggingFilter(GatewayRepo gatewayRepo, GatewayServiceConfig config) {
        this.gatewayRepo = gatewayRepo;
        this.config = config;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        /*
         * Get information of request from exchange
         * - get request and get header get information
         * - and you will add it to the BlockingQueue
         * 
         * After adding object to blockingQueue
         * - you will check if the size of the blocking queue is greater or equal to the
         * max Lock size
         * - if true call the drainRequests() function
         * 
         */

        Request rq = new Request()
                .setPath(exchange.getRequest().getPath().toString())
                .setIp_address(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress())
                .setCall_time(Instant.now());

        requests.add(rq);

        if (requests.size() >= config.getMaxLogs()) {
            drainRequests();
        }

        // exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        // always end with this
        return chain.filter(exchange);
    }

    // Nothing needed to be added here
    @Override
    public int getOrder() {
        return -1;
    }

    // Drain the request from the blocking queue to a normal list
    // Calls repo.insert that will run insertion of multiple objects at the same
    // time
    public void drainRequests() {
        List<Request> drainRequest = new ArrayList<>();
        requests.drainTo(drainRequest);
        gatewayRepo.insertRequests(drainRequest)
                .subscribeOn(DB_SCHEDULER) // This just says "where" to subscibe on
                                           // (there is a DB_SCHEDULER given to you in this class for this)
                .subscribe();
    }
}