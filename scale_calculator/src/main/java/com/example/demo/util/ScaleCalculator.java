package com.example.demo.util;

import com.example.demo.common.Constant;
import com.example.demo.common.Scale;

import java.util.Arrays;

public class ScaleCalculator {

    // 음계 차이 확인
    // rootScalerhk calScale의 차이를 확인
    public static String calculatInterval(String rootScale, String calScale) {
        String resultScale = "";
        int resultOctave = 0;
        // 계산할 코드 확인
        Scale root = findScale(rootScale);
        Scale cal = findScale(calScale);
        // octave 확인
        int scaleValue = root.getValue() - cal.getValue();

        return "Scale : " + resultScale;
    }

    // 음계 찾기
    // enum으로 저장 된 음계 확인
    public static Scale findScale(String scale) {
        Scale result = null;
        if( (!"".equals(scale) && scale != null)){
            for(Scale s : Scale.values()) {
                if(s.getScale().equals(scale)) {
                    result = s;
                    break;
                }
            }
        }
        return result;
    }

    public static boolean checkSemitone(String[] arr) {
        boolean result = false;
        Arrays.asList(arr).containsAll(Arrays.asList(Constant.SEMITONE.MI_FA));
        return result;
    }
}
