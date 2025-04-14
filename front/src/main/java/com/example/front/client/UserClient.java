package com.example.front.client;

import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "backend")
public interface UserClient {
    @GetMapping("/books")
    Mono<String> getBookById(@RequestParam("id") Long id);
}