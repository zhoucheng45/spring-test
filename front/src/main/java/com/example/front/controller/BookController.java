package com.example.front.controller;

import com.example.front.client.BookClient;
import com.example.front.utils.DBIPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RequestMapping("/front")
@RestController
public class BookController {

    @Autowired
    private BookClient bookClient;


    @GetMapping("/books")
    public String getBookById(@RequestParam("id") Long id){
        log.info("getBookById:{}",id);
        return bookClient.getBookById(id);
    }


    @GetMapping("/ip/query")
    public Mono<Map<String, Object>> queryIp(@RequestParam("ip") String ip){
        Map<String, Object> stringObjectMap = DBIPUtils.queryIpInfo(ip);

        return Mono.just(stringObjectMap);
    }
}