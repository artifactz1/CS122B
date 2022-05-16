package com.github.klefstad_teaching.cs122b.gateway.routes;

import com.github.klefstad_teaching.cs122b.gateway.config.GatewayServiceConfig;
import com.github.klefstad_teaching.cs122b.gateway.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GatewayRouteLocator {
    private final GatewayServiceConfig config;
    private final AuthFilter authFilter;

    @Autowired
    public GatewayRouteLocator(GatewayServiceConfig config,
            AuthFilter authFilter) {
        this.config = config;
        this.authFilter = authFilter;
    }

    // You should have your services running when your running this service because
    // you want to be able to run all the calls

    // If you run the test, the test don't depend on any of the micro services being
    // open, so and so you actually have to have the services off.
    // -there is a service for mock services

    // idm doesn't need filters
    // but movies and billing do

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("idm",
                        r -> r.path("/idm/**")
                                .filters(f -> f.rewritePath("/idm", ""))
                                .uri(config.getIdm()))
                .route("movies",
                        r -> r.path("/movies/**")
                                .filters(f -> f.rewritePath("/movies/", ""))
                                .uri(config.getMovies()))
                .route("billing",
                        r -> r.path("/billing/**")
                                .uri(config.getBilling()))
                .build();
    }
}
