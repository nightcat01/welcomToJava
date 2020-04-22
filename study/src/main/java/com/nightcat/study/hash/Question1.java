package com.nightcat.study.hash;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Admin
 * User : Admin
 * Date : 2020-04-07 오후 3:14
 * Description :
 */
public class Question1 {

    /**
     *
     * 문제
     * 수많은 마라톤 선수들이 마라톤에 참여하였습니다. 단 한 명의 선수를 제외하고는 모든 선수가 마라톤을 완주하였습니다.
     * 마라톤에 참여한 선수들의 이름이 담긴 배열 participant와 완주한 선수들의 이름이 담긴 배열 completion이 주어질 때, 완주하지 못한 선수의 이름을 return 하도록 solution 함수를 작성해주세요.
     *
     * 제한사항
     * 마라톤 경기에 참여한 선수의 수는 1명 이상 100,000명 이하입니다.
     * completion의 길이는 participant의 길이보다 1 작습니다.
     * 참가자의 이름은 1개 이상 20개 이하의 알파벳 소문자로 이루어져 있습니다.
     * 참가자 중에는 동명이인이 있을 수 있습니다.
     *
     * 예제 #1
     * leo는 참여자 명단에는 있지만, 완주자 명단에는 없기 때문에 완주하지 못했습니다.
     *
     * 예제 #2
     * vinko는 참여자 명단에는 있지만, 완주자 명단에는 없기 때문에 완주하지 못했습니다.
     *
     * 예제 #3
     * mislav는 참여자 명단에는 두 명이 있지만, 완주자 명단에는 한 명밖에 없기 때문에 한명은 완주하지 못했습니다.
     *
     */

    final public String[] participantGroupFirst = {"leo", "kiki", "eden"};
    final public String[] participantGroupSecond = {"marina", "josipa", "nikola", "vinko", "filipa"};
    final public String[] participantGroupThird = {"mislav", "stanko", "mislav", "ana"};

    final public String[] completionGroupFirst = {"eden", "kiki"};
    final public String[] completionGroupSecond = {"josipa", "filipa", "marina", "nikola"};
    final public String[] completionGroupThird = {"stanko", "ana", "mislav"};

    final public String groupFirstAnswer = "leo";
    final public String groupSecondAnswer = "vinko";
    final public String groupThirdAnswer = "mislav";


    public String solution(String[] participant, String[] completion) {
        String answer = "";

        // 내 답********************
        Arrays.sort(participant);
        Arrays.sort(completion);

        // 동명이인이 있을 수 있기 때문에 1개씩 제거
        for(int i = 0; i < participant.length; i++) {
            int j = i;

            if(i > completion.length - 1) {
                j = 0; // 정렬 후 범위 검색을 실행하였으나, 답이 없을 경우 다시 처음으로 돌려 반드시 빠져나갈 수 있게 한다.
            }

            if(!participant[i].equals(completion[j])) {
                answer = participant[i];
                break;
            }
        }

        // 내 답********************

        // 맞다고 생각되는 답 ********************
        HashMap<String, Integer> hm = new HashMap<>();
        // getOrDefault로 인해 map이 같은 키를 덮어쓰는 것 회피
        for (String player : participant) hm.put(player, hm.getOrDefault(player, 0) + 1);
        for (String player : completion) hm.put(player, hm.get(player) - 1);

        for (String key : hm.keySet()) {
            if (hm.get(key) != 0){
                answer = key;
            }
        }
        // 맞다고 생각되는 답 ********************

        return answer;
    }
}
