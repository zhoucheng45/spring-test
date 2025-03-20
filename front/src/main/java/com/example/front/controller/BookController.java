package com.example.front.controller;

import com.example.front.client.BookClient;
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

@RestController
public class BookController {

    @Autowired
    private BookClient bookClient;

    @GetMapping("/front/books")
    public String getBookById(@RequestParam("id") Long id){

//        final URL url;
//        try {
//            url = new URL("http://localhost:28082/books?id=1");
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//        final HttpURLConnection connection;
//        try {
//            connection = (HttpURLConnection) url.openConnection();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        connection.setConnectTimeout(10000);
//        connection.setReadTimeout(10000);
//        connection.setAllowUserInteraction(false);
//        connection.setInstanceFollowRedirects(true);
//        try {
//            connection.setRequestMethod("GET");
//        } catch (ProtocolException e) {
//            throw new RuntimeException(e);
//        }
//
//        Collection<String> contentEncodingValues = null;
//        boolean gzipEncodedRequest = false;
//        boolean deflateEncodedRequest = false;
//        connection.addRequestProperty("Accept", "*/*");
//        int responseCode = 0;
//        try {
//            responseCode = connection.getResponseCode();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("==============="+responseCode);
//        try {
//            System.out.println("==============="+connection.getResponseMessage());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        return bookClient.getBookById(id);
    }
}