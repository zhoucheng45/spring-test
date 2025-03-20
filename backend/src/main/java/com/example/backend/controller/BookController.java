package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.entity.Book;
import com.example.backend.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {

    @Autowired
    private BookMapper bookMapper;

    @GetMapping("/books")
    public Book getBookById(@RequestParam("id") Long id) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return bookMapper.selectOne(queryWrapper);
    }
}