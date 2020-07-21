package com.nightcat.study.stackQueue;

import java.util.*;

/**
 * Created by Admin
 * User : Admin
 * Date : 2020-04-10 오후 1:30
 * Description :
 */
public class Question2 {
    /**
     *
     * 문제
     * 트럭 여러 대가 강을 가로지르는 일 차선 다리를 정해진 순으로 건너려 합니다. 모든 트럭이 다리를 건너려면 최소 몇 초가 걸리는지 알아내야 합니다. 트럭은 1초에 1만큼 움직이며, 다리 길이는 bridge_length이고 다리는 무게 weight까지 견딥니다.
     * ※ 트럭이 다리에 완전히 오르지 않은 경우, 이 트럭의 무게는 고려하지 않습니다.
     *
     * 예를 들어, 길이가 2이고 10kg 무게를 견디는 다리가 있습니다. 무게가 [7, 4, 5, 6]kg인 트럭이 순서대로 최단 시간 안에 다리를 건너려면 다음과 같이 건너야 합니다.
     *
     * 경과 시간	        다리를 지난 트럭	다리를 건너는 트럭	대기 트럭
     * 0	[]	        []	            [7,4,5,6]
     * 1~2	[]	        [7]	            [4,5,6]
     * 3	[7]	        [4]	            [5,6]
     * 4	[7]	        [4,5]	        [6]
     * 5	[7,4]	    [5]	            [6]
     * 6~7	[7,4,5]	    [6]	            []
     * 8	[7,4,5,6]	[]	            []
     * 따라서, 모든 트럭이 다리를 지나려면 최소 8초가 걸립니다.
     *
     * solution 함수의 매개변수로 다리 길이 bridge_length, 다리가 견딜 수 있는 무게 weight, 트럭별 무게 truck_weights가 주어집니다. 이때 모든 트럭이 다리를 건너려면 최소 몇 초가 걸리는지 return 하도록 solution 함수를 완성하세요.
     *
     * 제한사항
     * bridge_length는 1 이상 10,000 이하입니다.
     * weight는 1 이상 10,000 이하입니다.
     * truck_weights의 길이는 1 이상 10,000 이하입니다.
     * 모든 트럭의 무게는 1 이상 weight 이하입니다.
     *
     * 입출력 예
     * bridge_length	weight	truck_weights	                    return
     * 2	            10	    [7,4,5,6]	                        8
     * 100	            100	    [10]	                            101
     * 100	            100	    [10,10,10,10,10,10,10,10,10,10]     110
     *
     */

    final public int bridge_length1 = 2;
    final public int bridge_length2 = 100;
    final public int bridge_length3 = 100;

    final public int weight1 = 10;
    final public int weight2 = 10;
    final public int weight3 = 100;

    final public int[] truck_weights1 = {7,4,5,6};
    final public int[] truck_weights2 = {10};
    final public int[] truck_weights3 = {10,10,10,10,10,10,10,10,10,10};

    final public int answer1 = 8;
    final public int answer2 = 101;
    final public int answer3 = 110;


    public int solution(int bridge_length, int weight, int[] truck_weights) {
        int answer = 0;

        // 내 답********************

        // 무게 7인 차 1대가 길이 2, 무게 10인 다리를 건널 때 걸리는 시간 3초
        // 차 1대는 초당 길이 1을 지나갈 수 있음. 1길이/1초 + 1(다리를 빠져나감)
        // 무게가 초과하지 않는 경우 차는 동시에 다리 위에 존재할 수 있다.
        // FIFO 구성으로 queue를 이용하여 풀이

        Queue<Truck> bridge = new LinkedList<>();

        int index = 0; // 트럭 대기열
        int totWeight = 0; // 다리에 올라가있는 총 무게
        int length = truck_weights.length;
        boolean isProcess = true;
        int truckWeight = 0;
        while(isProcess) { // 대기열이 다 사라질 때까지
            Truck delTruck = null;
            // 다리 위에 있는 트럭들이 한칸씩 전진
            for(Truck t : bridge) {
                int inBridgeTruckTime = t.getTime();
                System.out.println(t.getWeight() + "    " + inBridgeTruckTime);
                // 다리길이보다 더 나갔을 경우 다리를 빠져나갔다고하여 제거
                if(bridge_length < ++inBridgeTruckTime) {
                    System.out.println(t.getWeight() + "    " + inBridgeTruckTime);
                    totWeight -= t.getWeight();
                    delTruck = t;
                } else { // 아닐 경우 전진
//                    System.out.println(t.getWeight() + "    " + inBridgeTruckTime);
                    t.setTime(inBridgeTruckTime);
                }
            }

            // 삭제할 트럭 정보 삭제
            ((LinkedList<Truck>) bridge).remove(delTruck);

            if(index < length) {
                truckWeight = truck_weights[index];

                // 현재 트럭 무게 + 다리 위 트럭 무게 비교
                if (weight >= totWeight + truckWeight) {
                    Truck truck = new Truck();
                    truck.setWeight(truckWeight);
                    truck.setTime(1);
                    bridge.offer(truck);
                    totWeight += truckWeight;
                    index++;
                }
            }
            // 트럭들이 한칸씩 전진 후에 1초 증가
            answer++;

            System.out.println(index + "      " + bridge + "    " + totWeight + "      " + bridge.size() + "    " + answer);

            if(bridge.size() == 0) {
                isProcess = false;
            }

        }

        // 내 답********************

        return answer;
    }
}

class Truck {
    int weight;
    int time;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}