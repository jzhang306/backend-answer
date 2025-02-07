package com.test.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class testController {
    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }
}
