package com.nightcat.reactive.reactivesample.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SampleRestControllerTest {

    @Test
    public void titleFix() {
        Flux<String> flux = Flux.just("A", "B", "C");
//        Flux<String> flux = Flux.fromArray(new String[]{"A", "B", "C"});
//        Flux<String> flux = Flux.fromIterable(Arrays.asList("A", "B", "C"));

    //To subscribe call method

        flux.log().subscribe();
    }

}
