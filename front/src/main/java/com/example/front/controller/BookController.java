package com.example.front.controller;

import com.example.front.client.BookClient;
import com.example.front.client.UserClient;
import com.example.front.utils.DBIPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequestMapping("/front")
@RestController
public class BookController {

    @Autowired
    private BookClient bookClient;

    @Autowired
    private UserClient userClient;

    @GetMapping("/books")
    public Mono<String> getBookById(@RequestParam("id") Long id){
        Mono<String> bookById = userClient.getBookById(id);
        return bookById;
    }


    @GetMapping("/ip/query")
    public Mono<Map<String, Object>> queryIp(@RequestParam("ip") String ip){
        Map<String, Object> stringObjectMap = DBIPUtils.queryIpInfo(ip);

        return Mono.justOrEmpty(stringObjectMap);
    }
}