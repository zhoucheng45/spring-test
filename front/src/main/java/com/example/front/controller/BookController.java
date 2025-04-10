package com.example.front.controller;

import com.example.front.client.BookClient;
import com.example.front.utils.DBIPUtils;
import com.maxmind.geoip2.record.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Country queryIp(@RequestParam("ip") String ip) throws Exception {
        Country country = DBIPUtils.getCountry(ip);

        System.out.println(country);
        return country;
    }
}