package com.zifty.lambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

@Configuration
public class LambdaHandler {
    private static final Logger log = LoggerFactory.getLogger(LambdaHandler.class);

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }

    public record Request(String name, String data) {}

    @Bean
    public Function<Mono<Request>, Mono<Map<String, Object>>> handleRequest(WebClient webClient) {
        log.info("handleRequest");
        return request -> request
                .flatMap(data -> webClient.post()
                        .uri("/posts")
                        .bodyValue(data)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>(){}))
                .doOnNext(s -> log.info("response: {}", s));
    }
}
