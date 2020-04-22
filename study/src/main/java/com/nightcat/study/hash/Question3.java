package com.nightcat.study.hash;

import java.util.HashMap;

/**
 * Created by Admin
 * User : Admin
 * Date : 2020-04-07 오후 3:14
 * Description :
 */
public class Question3 {

    /**
     *
     * 문제
     * 스파이들은 매일 다른 옷을 조합하여 입어 자신을 위장합니다.
     *
     * 예를 들어 스파이가 가진 옷이 아래와 같고 오늘 스파이가 동그란 안경, 긴 코트, 파란색 티셔츠를 입었다면 다음날은 청바지를 추가로 입거나 동그란 안경 대신 검정 선글라스를 착용하거나 해야 합니다.
     *
     * 종류	이름
     * 얼굴	동그란 안경, 검정 선글라스
     * 상의	파란색 티셔츠
     * 하의	청바지
     * 겉옷	긴 코트
     * 스파이가 가진 의상들이 담긴 2차원 배열 clothes가 주어질 때 서로 다른 옷의 조합의 수를 return 하도록 solution 함수를 작성해주세요.
     *
     * 제한 사항
     * clothes의 각 행은 [의상의 이름, 의상의 종류]로 이루어져 있습니다.
     * 스파이가 가진 의상의 수는 1개 이상 30개 이하입니다.
     * 같은 이름을 가진 의상은 존재하지 않습니다.
     * clothes의 모든 원소는 문자열로 이루어져 있습니다.
     * 모든 문자열의 길이는 1 이상 20 이하인 자연수이고 알파벳 소문자 또는 '_' 로만 이루어져 있습니다.
     * 스파이는 하루에 최소 한 개의 의상은 입습니다.
     *
     * 예제 #1
     * headgear에 해당하는 의상이 yellow_hat, green_turban이고 eyewear에 해당하는 의상이 blue_sunglasses이므로 아래와 같이 5개의 조합이 가능합니다.
     *
     * 1. yellow_hat
     * 2. blue_sunglasses
     * 3. green_turban
     * 4. yellow_hat + blue_sunglasses
     * 5. green_turban + blue_sunglasses
     *
     * 예제 #2
     * face에 해당하는 의상이 crow_mask, blue_sunglasses, smoky_makeup이므로 아래와 같이 3개의 조합이 가능합니다.
     *
     * 1. crow_mask
     * 2. blue_sunglasses
     * 3. smoky_makeup
     *
     * clothes	                                                                        return
     * [[yellow_hat, headgear], [blue_sunglasses, eyewear], [green_turban, headgear]]	5
     * [[crow_mask, face], [blue_sunglasses, face], [smoky_makeup, face]]	            3
     *
     */

    final public String[][] clothGroupFirst = {{"yellow_hat", "headgear"}, {"blue_sunglasses", "eyewear"}, {"green_turban", "headgear"}};
    final public String[][] clothGroupSecond = {{"crow_mask", "face"}, {"blue_sunglasses", "face"}, {"smoky_makeup", "face"}};

    final public int groupFirstAnswer = 5;
    final public int groupSecondAnswer = 3;

    public int solution(String[][] clothes) {
        int answer = 1;

        // 내 답********************
        // 하나의 종류도 갯수로 포함하는 것으로 계산
        // 아무것도 안 입는 경우는 없어서 -1
        // ((종류1 + 1) * (종류2 + 1) * ....) - 1 = 답
        // 이 문제 풀면서 느낀 점은 꽤 새삼스럽다.
        // 로직을 짠다 = 공식을 세운다
        // 뭐.. 조금 다르겠지만 대략적인 느낌은 위와 같다.
        HashMap<String, Integer> map = new HashMap<>();

        for(String[] arr : clothes) {
            map.put(arr[1], map.getOrDefault(arr[1], 0) + 1);
        }

        for(Integer i : map.values()) {
            answer *= (i + 1);
        }
        answer -= 1;

        // 내 답********************

        System.out.println(answer);

        return answer;
    }
}
