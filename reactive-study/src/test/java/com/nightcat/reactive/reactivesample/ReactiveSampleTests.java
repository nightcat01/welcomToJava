package com.nightcat.reactive.reactivesample;

import com.nightcat.reactive.reactivesample.domain.FruitInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactiveSampleTests {

    public List<String> basket1 = Arrays.asList(new String[]{"kiwi", "orange", "lemon", "orange", "lemon", "kiwi"});
    public List<String> basket2 = Arrays.asList(new String[]{"banana", "lemon", "lemon", "kiwi"});
    public List<String> basket3 = Arrays.asList(new String[]{"strawberry", "orange", "lemon", "grape", "strawberry"});
    public List<List<String>> baskets = Arrays.asList(basket1, basket2, basket3);
    public Flux<List<String>> basketFlux = Flux.fromIterable(baskets);

    @Test
    public void fruitsBasket() {

        // 목표 : 각 바구니 별로 중복 없이 과일 정렬, 각 과일의 갯수 출력

        /*
        * 값을 꺼내서 새로운 Publisher로 바꿔줄 수 있는 연산자로는 대표적으로
        * flatMap : return 값의 순서 보장하지 않음.
        * flatMapSequential :
        *   return 값의 순서 보장.
        *   일단 오는 대로 구독하고 결과는 순서에 맞게 리턴
        *   비동기 환경에서 동시성을 지원하면서도 순서를 보장
        * concatMap :
        *   return 값의 순서 보장.
        *   인자로 지정된 함수에서 리턴하는 Publisher의 스트림이 다 끝난 후 그다음 넘어오는 값의 Publisher스트림 처리
        * */

        basketFlux.concatMap(basket -> {
            final Mono<List<String>> distinctFruits =
                    Flux.fromIterable(basket) // List<T> 형태를 Flux로 변경
                        .distinct() // 중복 제거
                        .collectList(); // Mono<List<T>> 형태로 반환
            final Mono<Map<String, Long>> countFruitsMono = Flux.fromIterable(basket)
                    .groupBy(fruit -> fruit) // 바구니로 부터 넘어온 과일 기준으로 group을 묶는다.
                    .concatMap(groupedFlux -> groupedFlux.count() // Mono<Long>
                            .map(count -> {
                                final Map<String, Long> fruitCount = new LinkedHashMap<>();
                                fruitCount.put(groupedFlux.key(), count);

                                return fruitCount;
                            }) // 각 과일별로 개수를 Map으로 리턴
                    ) // concatMap으로 순서보장
                    .reduce((accumulatedMap, currentMap) -> new LinkedHashMap<String, Long>() { {
//                        putAll(accumulatedMap);
                        putAll(currentMap);
                    }}); // 그동안 누적된 accumulatedMap에 현재 넘어오는 currentMap을 합쳐서 새로운 Map을 만든다. // map끼리 putAll하여 하나의 Map으로 만든다.
            return Flux.zip(distinctFruits, countFruitsMono, (distinct, count) -> new FruitInfo(distinct, count));
        }).subscribe(System.out::println);

    }

}
