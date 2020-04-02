package com.nightcat.reactive.reactivesample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping(value = "sample")
public class SampleController {

    @GetMapping(value = "hello")
    public String hello(final Model model) {
        model.addAttribute("hello", Mono.just("hello"));
        return "/hello";
    }


    @GetMapping(value = "title")
    public String title(final Model model) {
        model.addAttribute("title", Flux.just("세키로", "다크소울", "블러드본"));
        return "/title";
    }


}
