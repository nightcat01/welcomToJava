package com.nightcat.reactive.reactivesample;

import com.nightcat.reactive.reactivesample.domain.FruitInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FruitsBasketTest {

    final List<String> basket1 = Arrays.asList(new String[]{"kiwi", "orange", "lemon", "orange", "lemon", "kiwi"});
    final List<String> basket2 = Arrays.asList(new String[]{"banana", "lemon", "lemon", "kiwi"});
    final List<String> basket3 = Arrays.asList(new String[]{"strawberry", "orange", "lemon", "grape", "strawberry"});
    final List<List<String>> baskets = Arrays.asList(basket1, basket2, basket3);
    final Flux<List<String>> basketFlux = Flux.fromIterable(baskets);

    @Test
    public void fruitsBasket_reactor_block() {
        // 목적 : 과일들을 중복없이 순서대로 나열하고 각 과일의 갯수 출력

        /*
        flux의 값을 꺼낼 수 있는 것은 여러가지가 있으나,
        대표적으로 flatMap, flatMapSequential, concatMap이 사용된다.
        flatMap은 순서를 보장하지 않으므로, 나머지 두개 중 하나를 사용한다.

        flatMapSequential과 concatMap의 차이
        concatMap : 인자로 지정된 함수에서 리턴하는 Publisher의 스트림이 다 끝난 후 그다음 넘어오는 값의 Publisher스트림을 처리
        flatMapSequential : 일단 오는 대로 구독하고 결과는 순서에 맞게 리턴.
                            비동기 환경에서 동시성을 지원하면서도 순서를 보장
        */

        /*
        * 아래 방식은 동기, 블록킹 방식 처리로 우리가 원하는 비동기 형식과는 맞지 않는 방식의 처리
        * 단순히 reactor를 사용해서 처리한 것뿐 효율이 좋지 않다.
        * 스레드 동작 여부를 확인하면 알 수 있을거라함.
        */

        basketFlux.concatMap(basket -> {
                final Mono<List<String>> distinctFruits =
                        Flux  .fromIterable(basket) // List<String>을 Flux로 변환
                              .distinct() // 중복 제거
                              .collectList() // Mono<List<String>>으로 반환
                        ;
                final Mono<Map<String, Long>> countFruits =
                        Flux.fromIterable(basket) // List<String>을 Flux로 변환
                            .groupBy(fruit -> fruit) // 바구니에서 넘어온 과일 기준으로 group으로 묶는다
                            .concatMap(groupedFluxFruits -> groupedFluxFruits.count() // 그룹으로 반환 된 과일의 갯수를 센다.
                                .map(count -> {
                                        final Map<String, Long> fruitCount = new LinkedHashMap<>(); // 과일 별 갯수를 저장하기 위해 LinkedHashMap 생성
                                        fruitCount.put(groupedFluxFruits.key(), count); // 그룹으로 반환 된 과일 이름과 갯수를 저장
                                        return fruitCount;
                                    }) // 각 과일 별로 갯수를 Map에 저장하여 return
                                ) // concatMap으로 순서 보장
                                .reduce((accumulatedMap, currentMap) -> // accumulatedMap : 두번째 이후 인자를 누적하는 용도로 사용됨, currentMap : 위에서 내려받아진 Map
                                                // reduce 사용 방법 참고 https://javacan.tistory.com/entry/Reactor-Start-8-Aggregation
                                            new LinkedHashMap<String, Long>() { { // LinkedHashMap에 대한 method를 '{{}}' 처리 하는 것으로 두개 생성함
                                                putAll(accumulatedMap); // accumulatedMap.putAll(accumulatedMap.put(currentMap)) .... 대략 이런 느낌으로..
                                                putAll(currentMap); // currentMap.putAll(currentMap) ... 이것도.. 아마 맞을려나
                                            }}
                                        ); // 그동안 누적된 accumulatedMap에 현재 넘어오는 currentMap을 합쳐서 새로운 Map을 만든다. // map끼리 putAll하여 하나의 Map으로 만든다.
                return Flux.zip(distinctFruits, countFruits, (distinct, count) -> new FruitInfo(distinct, count));
        }).subscribe(System.out::println); // :: 이중콜론 -> A::B 면 A.B()와 같다고 생각하면 될 것 같다.

    }

    @Test
    public void fruitsBasket_reactor_nonBlock_noResult() {
        /*
        * 병렬 스케줄러(Schedulers.parallel())을 사용
        * CPU 코어 수만큼 워커를 만들어 병렬 실행(RxJava에서는 Schedulers.computation()이 해당 스케줄러에 해당)
        * 병렬 스케줄러는 데몬 스레드를 사용
        *
        * subscribeOn으로 스케줄러 전환하기
        * subscribeOn연산자는 해당 스트림을 구독할 때 동작하는 스케줄러를 지정
        * distinctFruits와 countFruitsMono가 각각 병렬로 동작하길 원하므로 subscribeOn(Schedulers.parallel())을 각각 추가하여 실행
        *
        * 데몬스레드
        * 다른 일반 쓰레드(데몬 쓰레드가 아닌)의 작업을 돕는 보조적인 역할을 수행하는 쓰레드이다.
        * 일반 쓰레드가 모두 종료되면 데몬 쓰레드는 강제적으로 자동종료된다.
        * 데몬쓰레드가 생성한 쓰레드는 자동으로 데몬 쓰레드가 된다.
        * 예) 가비지컬렉터, 워드프로세서의 자동저장, 화면자동갱신 등
        *
        * 아래 소스는 결과를 내보내지 않는다.
        * 이유는 처음 아래 소스를 동작시키는 것은 일반 스레드임에 비해 병렬 스케줄러는 데몬스레드를 사용하는데,
        * 일반 스레드(main메서드)가 끝나버리면 데몬스레드도 함께 끝나버리기 때문이다.
        *
        */

        basketFlux.concatMap(basket -> {
            final Mono<List<String>> distinctFruits = Flux.fromIterable(basket).distinct().collectList().subscribeOn(Schedulers.parallel()); // .subscribeOn(Schedulers.parallel()) 을 사용하여 각각 실행
            final Mono<Map<String, Long>> countFruitsMono = Flux.fromIterable(basket)
                    .groupBy(fruit -> fruit) // 바구니로 부터 넘어온 과일 기준으로 group을 묶는다.
                    .concatMap(groupedFlux -> groupedFlux.count()
                            .map(count -> {
                                final Map<String, Long> fruitCount = new LinkedHashMap<>();
                                fruitCount.put(groupedFlux.key(), count);
                                return fruitCount;
                            }) // 각 과일별로 개수를 Map으로 리턴
                    ) // concatMap으로 순서보장
                    .reduce((accumulatedMap, currentMap) -> new LinkedHashMap<String, Long>() { {
                        putAll(accumulatedMap);
                        putAll(currentMap);
                    }}) // 그동안 누적된 accumulatedMap에 현재 넘어오는 currentMap을 합쳐서 새로운 Map을 만든다. // map끼리 putAll하여 하나의 Map으로 만든다.
                    .subscribeOn(Schedulers.parallel()); // .subscribeOn(Schedulers.parallel()) 을 사용하여 각각 실행
            return Flux.zip(distinctFruits, countFruitsMono, (distinct, count) -> new FruitInfo(distinct, count));
        }).subscribe(System.out::println);
    }

    @Test
    public void fruitsBasket_reactor_nonBlock_getResult() throws InterruptedException {
        /*
         * 병렬 스케줄러(Schedulers.parallel())을 사용
         * CPU 코어 수만큼 워커를 만들어 병렬 실행(RxJava에서는 Schedulers.computation()이 해당 스케줄러에 해당)
         * 병렬 스케줄러는 데몬 스레드를 사용
         *
         * subscribeOn으로 스케줄러 전환하기
         * subscribeOn연산자는 해당 스트림을 구독할 때 동작하는 스케줄러를 지정
         * distinctFruits와 countFruitsMono가 각각 병렬로 동작하길 원하므로 subscribeOn(Schedulers.parallel())을 각각 추가하여 실행
         *
         * 데몬스레드
         * 다른 일반 쓰레드(데몬 쓰레드가 아닌)의 작업을 돕는 보조적인 역할을 수행하는 쓰레드이다.
         * 일반 쓰레드가 모두 종료되면 데몬 쓰레드는 강제적으로 자동종료된다.
         * 데몬쓰레드가 생성한 쓰레드는 자동으로 데몬 쓰레드가 된다.
         * 예) 가비지컬렉터, 워드프로세서의 자동저장, 화면자동갱신 등
         *
         * CountDownLatch를 이용
         * await()으로 애플리케이션이 종료되지 않게 main스레드가 동작이 끝날 때까지 대기
         * 스트림이 정상적으로 또는 에러가 나서 종료한 경우에 countDown메서드를 호출하여 더 이상 기다리지 않고 종료
         * 혹시 모르게 오랫동안 기다리는 경우를 막기 위해 await(2, TimeUnit.SECONDS)으로 2초 정도의 timeout
         * 참고 사이트 : https://dev.re.kr/52
         *
         */
        CountDownLatch countDownLatch = new CountDownLatch(10);

        basketFlux.concatMap(basket -> {
            final Mono<List<String>> distinctFruits = Flux.fromIterable(basket)
                                                            .log() // log 출력을 위한 메서드
                                                            .distinct()
                                                            .collectList()
                                                            .subscribeOn(Schedulers.parallel()); // .subscribeOn(Schedulers.parallel()) 을 사용하여 각각 실행
            final Mono<Map<String, Long>> countFruitsMono = Flux.fromIterable(basket)
                    .log() // log 출력을 위한 메서드
                    .groupBy(fruit -> fruit) // 바구니로 부터 넘어온 과일 기준으로 group을 묶는다.
                    .concatMap(groupedFlux -> groupedFlux.count()
                            .map(count -> {
                                final Map<String, Long> fruitCount = new LinkedHashMap<>();
                                fruitCount.put(groupedFlux.key(), count);
                                return fruitCount;
                            }) // 각 과일별로 개수를 Map으로 리턴
                    ) // concatMap으로 순서보장
                    .reduce((accumulatedMap, currentMap) -> new LinkedHashMap<String, Long>() { {
                        putAll(accumulatedMap);
                        putAll(currentMap);
                    }}) // 그동안 누적된 accumulatedMap에 현재 넘어오는 currentMap을 합쳐서 새로운 Map을 만든다. // map끼리 putAll하여 하나의 Map으로 만든다.
                    .subscribeOn(Schedulers.parallel()); // .subscribeOn(Schedulers.parallel()) 을 사용하여 각각 실행
            return Flux.zip(distinctFruits, countFruitsMono, (distinct, count) -> new FruitInfo(distinct, count));
        }).subscribe(
                System.out::println,  // 값이 넘어올 때 호출 됨, onNext(T)
                error -> { // error일 경우
                    System.err.println(error);
                    countDownLatch.countDown(); // countDownLatch 생성 시 지정한 수만큼 countDown을 실행
                }, // 에러 발생시 출력하고 countDown, onError(Throwable)
                () -> {
                    System.out.println("complete");
                    countDownLatch.countDown();
                } // 정상적 종료시 countDown, onComplete()
        );
//        countDownLatch.await(); // countDownLatch의 설정 count가 0이 될 때까지 기다린다.
        // 만약 0이 되지 않는다면 아래 메서드를 사용하여 특정시간 뒤에 0으로 만든다.
        countDownLatch.await(2, TimeUnit.SECONDS); // countDownLatch의 설정 count를 2초 뒤에 0으로 만든다.
    }
}
