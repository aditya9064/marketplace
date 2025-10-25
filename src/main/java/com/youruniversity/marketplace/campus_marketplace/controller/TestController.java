package com.youruniversity.marketplace.campus_marketplace.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/api/test")
    public String test() {
        return "Server is running!";
    }
}
