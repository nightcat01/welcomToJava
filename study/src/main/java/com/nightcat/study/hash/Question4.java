package com.nightcat.study.hash;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Admin
 * User : Admin
 * Date : 2020-04-07 오후 3:14
 * Description :
 */
public class Question4 {

    /**
     *
     * 문제
     * 스트리밍 사이트에서 장르 별로 가장 많이 재생된 노래를 두 개씩 모아 베스트 앨범을 출시하려 합니다. 노래는 고유 번호로 구분하며, 노래를 수록하는 기준은 다음과 같습니다.
     *
     * 속한 노래가 많이 재생된 장르를 먼저 수록합니다.
     * 장르 내에서 많이 재생된 노래를 먼저 수록합니다.
     * 장르 내에서 재생 횟수가 같은 노래 중에서는 고유 번호가 낮은 노래를 먼저 수록합니다.
     * 노래의 장르를 나타내는 문자열 배열 genres와 노래별 재생 횟수를 나타내는 정수 배열 plays가 주어질 때, 베스트 앨범에 들어갈 노래의 고유 번호를 순서대로 return 하도록 solution 함수를 완성하세요.
     *
     * 제한사항
     * genres[i]는 고유번호가 i인 노래의 장르입니다.
     * plays[i]는 고유번호가 i인 노래가 재생된 횟수입니다.
     * genres와 plays의 길이는 같으며, 이는 1 이상 10,000 이하입니다.
     * 장르 종류는 100개 미만입니다.
     * 장르에 속한 곡이 하나라면, 하나의 곡만 선택합니다.
     * 모든 장르는 재생된 횟수가 다릅니다.
     *
     * classic 장르는 1,450회 재생되었으며, classic 노래는 다음과 같습니다.
     *
     * 고유 번호 3: 800회 재생
     * 고유 번호 0: 500회 재생
     * 고유 번호 2: 150회 재생
     * pop 장르는 3,100회 재생되었으며, pop 노래는 다음과 같습니다.
     *
     * 고유 번호 4: 2,500회 재생
     * 고유 번호 1: 600회 재생
     * 따라서 pop 장르의 [4, 1]번 노래를 먼저, classic 장르의 [3, 0]번 노래를 그다음에 수록합니다.
     *
     * genres	                                plays	                    return
     * [classic, pop, classic, classic, pop]	[500, 600, 150, 800, 2500]	[4, 1, 3, 0]
     *
     */

    final public String[] genres = {"classic", "pop", "classic", "classic", "pop"};
    final public String[] genres2 = {"classic", "classic", "classic", "classic"};
    final public String[] genres3 = {"classic", "classic", "classic", "classic", "pop", "pop"};
    final public String[] genres4 = {"classic"};
    final public String[] genres5 = {"classic", "classic", "classic", "pop", "jazz", "pop", "jazz"};
    final public String[] genres6 = {"classic", "classic", "classic", "pop", "jazz", "pop", "jazz", "kpop"};

    final public int[] plays = {500, 600, 150, 800, 2500};
    final public int[] plays2 = {500, 600, 150, 2500};
    final public int[] plays3 = {2500, 600, 150, 2500, 100, 1000};
    final public int[] plays4 = {2500};
    final public int[] plays5 = {2500, 600, 150, 2400, 100, 1000, 850};
    final public int[] plays6 = {2500, 600, 150, 2400, 100, 1000, 850, 100};

    final public int[] answer = {4, 1, 3, 0};
    final public int[] answer2 = {3, 1};
    final public int[] answer3 = {0, 3, 5, 4};
    final public int[] answer4 = {0};
    final public int[] answer5 = {3, 5, 0, 1, 6, 4};
    final public int[] answer6 = {3, 5, 0, 1, 6, 4, 7};

    public int[] solution(String[] genres, int[] plays) {
        int[] answer = {};

        // 내 답********************

        // 속한 노래가 많이 재생 된 장르 S
        Map<String, Integer> lMap = new HashMap<>();
        Map<String, Integer> genreCntMap = new HashMap<>();
        for(int i = 0; i < genres.length; i++) {
            lMap.put(genres[i], lMap.getOrDefault(genres[i], 0) + plays[i]);
            genreCntMap.put(genres[i], genreCntMap.getOrDefault(genres[i], 0) + 1);
        }

        Object[] keyArr = lMap.keySet().toArray();
        Object[] valueArr = lMap.values().toArray();

        Arrays.sort(valueArr, Collections.reverseOrder());
        Map<String, Integer> resultMap = new LinkedHashMap<>();

        for(Object o : valueArr) {
            for(Object key : keyArr) {
                if(o == lMap.get(key)) {
                    resultMap.put((String) key, (Integer) o);
                }
            }
        }
        // 속한 노래가 많이 재생 된 장르 E

        // answerSize 생성 S
        Iterator genresKeyCnt = genreCntMap.keySet().iterator();
        int answerSize = 0;
        while(genresKeyCnt.hasNext()) {
            int value = genreCntMap.get(genresKeyCnt.next());
            if(value > 2) {
                answerSize += 2;
            } else {
                answerSize += value;
            }
        }

        answer = new int[answerSize];
        // answerSize 생성 E

        int index = 0;

        Iterator iterator = resultMap.keySet().iterator();

        while(iterator.hasNext()) {
            // 장르 내에서 많이 재생된 노래 S
            String key = (String) iterator.next();
            int max = 0;
            int secondMax = 0;
            int maxIndex = 0;
            int secondMaxIndex = 0;

            for(int i = 0; i < genres.length; i++) {
                if(key.equals(genres[i])) {
                    if(max <= plays[i]) {
                        max = plays[i];
                        maxIndex = i;
                    }
                }
            }

            for (int i = 0; i < genres.length; i++) {
                if (key.equals(genres[i]) && plays[i] <= max && maxIndex != i) {
                    if (secondMax <= plays[i]) {
                        secondMax = plays[i];
                        secondMaxIndex = i;
                    }
                }
            }
            // 장르 내에서 많이 재생된 노래 E

            // 장르 내 재생횟수가 같은 노래 중 고유번호가 낮은 노래 S
            if(max == secondMax && (secondMaxIndex >= 0 && maxIndex > secondMaxIndex)) {
                int temp = maxIndex;
                maxIndex = secondMaxIndex;
                secondMaxIndex = temp;
            }
//            System.out.println(max + ", " + maxIndex + "              " + secondMax + ", " + secondMaxIndex);

            // 장르 내 재생횟수가 같은 노래 중 고유번호가 낮은 노래 E
            answer[index] = maxIndex;
            index++;
            if(secondMax > 0 && index < answerSize) {
//                System.out.println(max + ", " + maxIndex + "              " + secondMax + ", " + secondMaxIndex);
                answer[index] = secondMaxIndex;
                index++;
            }
        }

        // 내 답********************
        System.out.println(lMap);
        for(int i = 0; i < answer.length; i++) {
            System.out.println(i + "        " + answer[i]);
        }

        return answer;
    }
}
