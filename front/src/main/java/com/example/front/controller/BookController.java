package com.example.front.controller;

import com.example.front.client.BookClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collection;

import static feign.Util.CONTENT_ENCODING;

@Slf4j
@RestController
public class BookController {

    @Autowired
    private BookClient bookClient;


    @GetMapping("/front/books")
    public String getBookById(@RequestParam("id") Long id){
        log.info("getBookById:{}",id);
        if(id == null) {
            int a = 0;
            int b = 4;
            var c = b / a;
        }
        return bookClient.getBookById(id);
    }
}