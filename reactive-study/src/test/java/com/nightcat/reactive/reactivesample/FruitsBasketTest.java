package com.nightcat.reactive.reactivesample;

import com.nightcat.reactive.reactivesample.domain.FruitInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FruitsBasketTest {

    // 과일바구니 테스트 http://tech.kakao.com/2018/05/29/reactor-programming/

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
        CountDownLatch countDownLatch = new CountDownLatch(10); // 설정한 카운트 동안 메인 스레드가 기다릴 수 있도록 해주는 객체

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

    @Test
    public void useOneStream() {
        // 목표 : basket당 하나의 스트림만 공유하며 과일종류와 개수 뽑아내기
        // 서브 목표 : 2개 구독자가 구독하면 자동으로 동작하게 Hot Flux 만들기

        /*
        * distinctFruits와 countFruitsMono 모두 Flux.fromIterable(basket)에서 출발하기 때문에
        * 병렬로 동작해도 baskets를 각각 순회하여 중복되는 동작이라는 점은 처음의 예제와 다를 게 없다.
        * 또한 몇 개 안 되는 데이터를 각각 병렬로 처리하는 것은 스레드를 생성하고 컨텍스트 스위칭을 하는 비용이 많이 들어 효율적이지 못하다.
        * baskets을 순회하는 공통 작업을 하나의 스트림에서 하는 방법을 확인할 수 있도록 코드를 작성해보자
        *
        * Hot, Cold 개념
        * Cold :    각 Flux나 Mono를 subscribe 할 때마다 매번 독립적으로 새로 데이터를 생성해서 동작
        *           subscribe호출 전까지 아무런 동작도 하지 않고, subscribe를 호출하면 새로운 데이터를 생성
        *           특별하게 Hot을 취급하는 연산자가 아닌 이상 Flux나 Mono는 Cold로 동작
        * Hot : 구독하기 전부터 데이터의 스트림이 동작.
        *       Hot에 해당하는 스트림을 여러 곳에서 구독을 하면 현재 스트림에서 나오는 값을 구독하는 구독자들은 동일하게 받을 수 있다.(1:N의 관계)
        *       구독여부와 상관없이 특정 시점에 값을 생성, 제어하여 구독자(Subscriber)들이 동일한 값을 받을 수 있도록 할 수 있습니다.
        *
        * Connectable Flux
        * Cold에서 Hot으로 바꾸기 위해서는 Connectable Flux로 변환하는 과정이 필요
        * 기본적으로 publish라는 연산자를 호출하면 변경 가능
        * 변환된 Flux에서 connect()라는 메서드를 호출 가능하며 이 메서드가 여러 구독자들이 Connectable Flux를 구독한 후 값을 생성하여 각 구독자에게 보내기 시작하게 하는 메서드
        * 따라서 우리는 목표인 distinctFruits와 countFruitsMono가 구독을 모두 완료한 후에 connect()를 호출할 수 있게 해주어야 한다.
        *
        * autoConnect나 refCount에 인자 값으로 최소 구독하는 구독자의 개수를 지정해서 이 개수가 충족되면 자동으로 값을 생성할 수 있게 연산자를 제공
        * autoConnect : 이름 그대로 최소 구독 개수를 만족하면 자동으로 connect()를 호출하는 역할
        * refCount : autoConnect가 하는 일에 더해, 구독하고 있는 구독자의 개수를 세다가 하나도 구독하는 곳이 없으면 기존 소스의 스트림도 구독을 해제하는 역할
        *
        * Hot 이후에 각 스트림만 비동기로 처리하기
        * 데이터가 많아지거나 현재 스레드가 다른 일을 하게 하기 위해서
        * distinct나 groupBy count 등의 연산을 하는 지점은 각각 비동기로 처리
        *
        * subscribeOn은 위의 경우 부적절
        * subscribeOn을 호출한 객체를 구독할 때 해당 스트림 전체가 해당 스케줄러로 다 바뀌기 때문에,
        * Hot인 source도 2개의 구독자가 구독을 하면 subscribeOn이 지정한 스레드에서 실행
        * 그러면 distinct와 count로 갈라져 나오는 부분도 같은 스레드에서 실행되기 때문
        * 이를 확인하기 위해 기존의 예제에서 distinctFruits와 countFruitsMono에 각각 subscribeOn(Schedulers.parallel())을 붙여서 실행
        * 처음엔 parallel-1, 2로 각각 실행되며 되는 것처럼 보이지만 결국 source는 parallel-1으로만 실행되다가
        * 서로 맞물리지 않아 도중에 끊기며 결과도 제대로 나오지 않는다.
        * subscribeOn으로 스케줄러를 지정하여 스위칭이 되면 이후 스트림은 계속 그 스케줄러에 의해 동작되기 때문이다.
        *
        */

//        CountDownLatch countDownLatch = new CountDownLatch(10); // 동일한 스레드를 사용할 것이기 때문에 필요 없다.

        basketFlux.concatMap(basket -> {
            final Flux<String> source = Flux.fromIterable(basket).log()
                                            .publish() // hot으로 변경
                                            .autoConnect(2); // 2번 구독 될 경우 자동으로 connect()를 호출
            final Mono<List<String>> distinctFruits = source.distinct().collectList() // autoConnect한 source를 구독할 경우
//            final Mono<List<String>> distinctFruits = Flux.fromIterable(basket).distinct().collectList() // autoConnect한 source를 구독하지 않을 경우
                                                            .log()
                                                            .subscribeOn(Schedulers.parallel()) // 비동기 처리 - 결과가 제대로 나오지 않음
                                                            ;
            final Mono<Map<String, Long>> countFruitsMono = source
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
                    .log()
                    .subscribeOn(Schedulers.parallel()) // 비동기 처리 - 결과가 제대로 나오지 않음
                    ;
            return Flux.zip(distinctFruits, countFruitsMono, (distinct, count) -> new FruitInfo(distinct, count));
        }).subscribe(
                System.out::println,  // 값이 넘어올 때 호출 됨, onNext(T)
                error -> { // 에러 일 경우
                    System.err.println(error); // 에러 발생시 출력
//                    countDownLatch.countDown();
                },
                () -> { // 메서드가 정상적으로 호출 되었을 경우
                    System.out.println("complete"); // 정상적 종료
//                    countDownLatch.countDown();
                }
        );
    }

    @Test
    public void useOneStreamByPublishOn() {
        // 목표 : basket당 하나의 스트림만 공유하며 과일종류와 개수 뽑아내기
        // 서브 목표 : 2개 구독자가 구독하면 자동으로 동작하게 Hot Flux 만들기

        /*
         * publishOn
         * 연산자가 호출된 위치 이후에 실행되는 연산자들은 publishOn에서 지정된 스케줄러에서 실행
         * 사용 순서가 중요, 아래쪽에 두게 되면 맨 처음 동작하는 부분이 비동기로 동작하지 않는다.
         *
         */

        basketFlux.concatMap(basket -> {
            final Flux<String> source = Flux.fromIterable(basket).log()
                    .publish() // hot으로 변경
                    .autoConnect(2) // 2번 구독 될 경우 자동으로 connect()를 호출
                    // .subscribeOn(Schedulers.single())를 사용하면 왠지 모르게 도중에 끊긴다.. 이건 나중에 이유를 찾아보자
                    // 예상으로는 위쪽에서 subscribe를 시도해서 끊기는건가 싶기도 하고..
//                    .subscribeOn(Schedulers.single()) // 호출할 때마다 같은 스레드를 유지. 매번 source를 구독할 때마다 같은 스레드에서 동작
                    ;
            final Mono<List<String>> distinctFruits = source
                    .publishOn(Schedulers.parallel()) // 비동기 처리
                    .distinct().collectList() // autoConnect한 source를 구독할 경우
                    .log()
//                    .publishOn(Schedulers.parallel()) // 비동기 처리 - 아래쪽에 두게 되면 정상적으로 동작하지 않음.
                    ;
            final Mono<Map<String, Long>> countFruitsMono = source
                    .publishOn(Schedulers.parallel()) // 비동기 처리
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
                    .log()
//                    .publishOn(Schedulers.parallel()) // 비동기 처리 - 아래쪽에 두게 되면 정상적으로 동작하지 않음.
                    ;
            return Flux.zip(distinctFruits, countFruitsMono, (distinct, count) -> new FruitInfo(distinct, count));
        }).subscribe(
                System.out::println,  // 값이 넘어올 때 호출 됨, onNext(T)
                error -> { // 에러 일 경우
                    System.err.println(error); // 에러 발생시 출력
                },
                () -> { // 메서드가 정상적으로 호출 되었을 경우
                    System.out.println("complete"); // 정상적 종료
                }
        );
    }

    // 종합 테스트
    @Test
    public void testFruitBaskets() {

        // 1. 만들어질 결과를 예상하여 미리 정보를 만들어둔다.
        // 2. 만든 메서드의 결과와 미리 만든 결과를 비교한다.

        // 첫번째 결과
        final FruitInfo expected1 = new FruitInfo(
                Arrays.asList("kiwi", "orange", "lemon"),
                new LinkedHashMap<String, Long>() { {
                    put("kiwi", 2L);
                    put("orange", 2L);
                    put("lemon", 2L);
                }}
        );
        // 두번째 결과
        final FruitInfo expected2 = new FruitInfo(
                Arrays.asList("banana", "lemon", "kiwi"),
                new LinkedHashMap<String, Long>() { {
                    put("banana", 1L);
                    put("lemon", 2L);
                    put("kiwi", 1L);
                }}
        );
        // 세번째 결과
        final FruitInfo expected3 = new FruitInfo(
                Arrays.asList("strawberry", "orange", "lemon", "grape"),
                new LinkedHashMap<String, Long>() { {
                    put("strawberry", 2L);
                    put("orange", 1L);
                    put("lemon", 1L);
                    put("grape", 1L);
                }}
        );

        // 결과 비교
        StepVerifier.create(getFruitsFlux())
                .expectNext(expected1)
                .expectNext(expected2)
                .expectNext(expected3)
                .verifyComplete();
    }

    // basket당 하나의 스트림만 공유하며 과일종류와 개수 뽑아내기
    public Flux<FruitInfo> getFruitsFlux() {
        return basketFlux.concatMap(basket -> {
            final Flux<String> source = Flux.fromIterable(basket).log()
                    .publish() // hot으로 변경
                    .autoConnect(2) // 2번 구독 될 경우 자동으로 connect()를 호출
                    // .subscribeOn(Schedulers.single())를 사용하면 왠지 모르게 도중에 끊긴다.. 이건 나중에 이유를 찾아보자
                    // 예상으로는 위쪽에서 subscribe를 시도해서 끊기는건가 싶기도 하고..
                    .subscribeOn(Schedulers.single()) // 호출할 때마다 같은 스레드를 유지. 매번 source를 구독할 때마다 같은 스레드에서 동작
                    ;
            final Mono<List<String>> distinctFruits = source
                    .publishOn(Schedulers.parallel()) // 비동기 처리
                    .distinct().collectList() // autoConnect한 source를 구독할 경우
                    .log()
                    ;
            final Mono<Map<String, Long>> countFruitsMono = source
                    .publishOn(Schedulers.parallel()) // 비동기 처리
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
                    .log()
                    ;
            return Flux.zip(distinctFruits, countFruitsMono, (distinct, count) -> new FruitInfo(distinct, count));
        }).log();
    }
}
