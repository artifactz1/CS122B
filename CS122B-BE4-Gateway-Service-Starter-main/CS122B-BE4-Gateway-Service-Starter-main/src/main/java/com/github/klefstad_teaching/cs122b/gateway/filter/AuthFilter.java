package com.github.klefstad_teaching.cs122b.gateway.filter;

import com.github.klefstad_teaching.cs122b.core.result.IDMResults;
import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.core.result.ResultMap;
import com.github.klefstad_teaching.cs122b.core.security.JWTAuthenticationFilter;
import com.github.klefstad_teaching.cs122b.gateway.config.GatewayServiceConfig;
import com.github.klefstad_teaching.cs122b.gateway.models.AuthResponse;
import com.github.klefstad_teaching.cs122b.gateway.models.MyCustomResultPOJO;
import com.github.klefstad_teaching.cs122b.gateway.models.Posts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/*
    For movie and builiding service we need to pass the authentication of the access token
    - we don't know if the accesstoken is valid 

    You are going to take the authorization header from the request
    - send a request to the IDM's serives /authenticate endpoint in order to validate our user

*/

@Component
public class AuthFilter implements GatewayFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

    private final GatewayServiceConfig config;
    private final WebClient webClient;

    @Autowired
    public AuthFilter(GatewayServiceConfig config) {
        this.config = config;
        this.webClient = WebClient.builder().build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Optional<String> accessToken = getAccessTokenFromHeader(exchange);

        /*
         * Success
         * - return chain.filter(exchange);
         * 
         * Fail
         * - return exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
         */

        if (!accessToken.isPresent()) {
            return setToFail(exchange);
        }
        return authenticate(accessToken.get())
                .flatMap(result -> result.code() == IDMResults.ACCESS_TOKEN_IS_VALID.code() ? chain.filter(exchange)
                        : setToFail(exchange));
    }

    // ************************************************************************************
    // SEEMS CORRECT
    // ************************************************************************************
    private Mono<Void> setToFail(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    // ************************************************************************************
    // SEEMS CORRECT
    // ************************************************************************************

    /**
     * Takes in a accessToken token and creates Mono chain that calls the idm and
     * maps the value to
     * a Result
     *
     * @param accessToken a encodedJWT
     * @return a Mono that returns a Result
     */
    private Mono<Result> authenticate(String accessToken) {

        Posts postsBody = new Posts()
                .setBody("Body")
                .setAccessToken(accessToken);

        return this.webClient
                .post()
                .uri(config.getIdmAuthenticate())
                .bodyValue(postsBody) // Request
                .retrieve()
                .bodyToMono(AuthResponse.class) // Response
                .map(result -> ResultMap.fromCode(result.getResult().getCode()));

    }

    // ************************************************************************************
    // Finished
    // ************************************************************************************
    private Optional<String> getAccessTokenFromHeader(ServerWebExchange exchange) {

        List<String> aTK = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (aTK == null || aTK.size() != 1) {
            return Optional.empty();
        }

        String accessToken = aTK.get(0);

        if (accessToken.startsWith(JWTAuthenticationFilter.BEARER_PREFIX)) {
            return Optional.of(accessToken.substring(JWTAuthenticationFilter.BEARER_PREFIX.length()));
        } else {
            return Optional.empty();
        }
    }
}
