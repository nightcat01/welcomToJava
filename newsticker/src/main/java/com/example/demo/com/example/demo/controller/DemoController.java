package com.example.demo.com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    @RequestMapping("/news")
    public String demo() {
        return "/newsticker/two.html";
    }

    @RequestMapping("/simpleTicker")
    public String simpleTicker() {
        return "/simpleTicker/demo.html";
    }

    @RequestMapping("/totalTicker")
    public String totalTicker() {
        return "/demo/totalTicker.html";
    }
}
