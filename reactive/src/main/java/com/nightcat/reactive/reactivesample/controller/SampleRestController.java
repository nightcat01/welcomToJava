package com.nightcat.reactive.reactivesample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/sample/")
public class SampleRestController {

    static List<String> titleList = new ArrayList<>();

    static {
        titleList.add("오딘 스피어");
        titleList.add("드래곤즈 크라운");
        titleList.add("흐어어ㅓ");
    }

    @GetMapping(value = "hello")
    public Mono<String> hello() {
        return Mono.just("hello");
    }

    @GetMapping(value = "title")
    public Flux<String> title() {
        // 각 배열 형태의 사용
        Flux.from(Flux.just("이런", "것도", "되고")).log();
        Flux.fromArray(new String[]{"요런", "것도", "되고"}).log();
        Flux<String> result3 = Flux.fromIterable(titleList);
        return result3;
    }

    @GetMapping(value = "titleFix")
    public Flux<String> titleFix() {
        return Flux.fromIterable(titleList).map(list -> list);
    }
}
