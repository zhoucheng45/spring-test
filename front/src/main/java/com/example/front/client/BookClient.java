package com.example.front.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "backend")
public interface BookClient {
    @GetMapping("/books")
    String getBookById(@RequestParam("id") Long id);
}