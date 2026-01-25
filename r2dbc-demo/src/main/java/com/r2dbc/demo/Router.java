package com.r2dbc.demo;

import com.r2dbc.demo.handler.ProviderModelHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Router {

    private final ProviderModelHandler providerModelHandler;

    private Mono<ServerResponse> handleException(Exception e, ServerRequest request) {
        log.info("Exception Occurred In Path: {}", request.path());
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue(e.getMessage());
    }

    @Bean
    public RouterFunction<ServerResponse> mainRouter() {
        return route()
                .path("/api/v1/providerModels", this::providerModelRouter)
                .onError(Exception.class, this::handleException)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> providerModelRouter() {
        return route()
                .GET("/getModelsGroupedByProvider", providerModelHandler::getModelsGroupedByProvider)
                .build();
    }
}
