package com.sse.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class SseService {
    private final List<String> dataList = List.of("hi","by","gfdg","dfgd","fdsg","fsd");

    public Flux<String> getDataFlux() {
        List<String> list = new ArrayList<>();
        for (int i =0;i<30;i++) {
            list.add("d"+i);
        }
        return Flux.fromIterable(list)
                .map(data -> {
                    if (data.contains("20")) throw new RuntimeException("wow");
                    return data;
                })
                .delayElements(Duration.ofMillis(100));
    }
}
