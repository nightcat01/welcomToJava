package com.example.demo;

import com.example.demo.common.Constant;
import com.example.demo.common.Scale;
import com.example.demo.util.CommonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScaleCalculatorTests {

    @Test
    public void findScaleTest() {
        Scale scale = findScaleFromCode("C");
        System.out.println(scale.toString());
        Assert.assertEquals("DO", scale.toString());
        Assert.assertEquals("C", scale.getScale());
        Assert.assertEquals(1, scale.getValue());
    }

    @Test
    public void calculatIntervalTest() throws Exception{
        System.out.println(calculatInterval("D","C"));
//        Assert.assertEquals(12, calculatInterval("C","C"));
    }

    @Test
    public void sortScaleTest() {
        String[] scaleArr = sortScale("F");
        String[] arr = {"F", "G", "A", "B", "C", "D", "E"};
        Assert.assertArrayEquals(arr, scaleArr);
    }

    @Test
    public void checkSemitoneTest() throws Exception {
        int result = checkSemitone(selectScale("B", "D"));
        Assert.assertEquals(1, result);
    }

    @Test
    public void checkIntervalTest() throws Exception {
        String result = checkInterval("B", "D");
        System.out.println("resutl : " + result);
        Assert.assertEquals(calculatInterval("B", "D") + " " + Constant.INTERVAL.MINOR, checkInterval("B", "D"));
        Assert.assertEquals(calculatInterval("C", "D") + " " + Constant.INTERVAL.MAJOR, checkInterval("C", "D"));
        Assert.assertEquals(calculatInterval("C", "F") + " " + Constant.INTERVAL.PERFECT, checkInterval("C", "F"));
    }

    // 음계 차이 확인
    // rootScalerhk calScale의 차이를 확인
    public static int calculatInterval(String rootScale, String calScale) throws Exception {
        int result;

        // 계산할 코드 확인
        Scale root = findScaleFromCode(rootScale);
        Scale cal = findScaleFromCode(calScale);

        int rootValue = root.getValue();
        int calValue = cal.getValue();

        if(rootValue >= calValue) {
            calValue = calValue + 7;
        }

        // 몇도 차이인지 계산
        int scaleValue = Math.abs(rootValue - calValue);

        // scale.value로 계산하면 1칸이 비기 때문에 +1
        result = scaleValue + 1;

        return result;
    }

    // 음계 찾기
    // enum으로 저장 된 음계 확인
    public static Scale findScaleFromCode(String scale) {
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

    // scale 정렬
    // 기준으로 잡은 scale 부터 스케일 배열 정렬
    // ex) 기준 : E
    // 원본 배열 : C-D-E-F-G-A-B
    // 정렬 배열 : E-F-G-A-B-C-D
    public static String[] sortScale(String rootScale) {
        String[] baseArr = Constant.SCALE_ARR;
        String[] result = baseArr;
        int index = Arrays.asList(baseArr).indexOf(rootScale);

        if(index > 0) {
            String[] addArr = Arrays.copyOfRange(result, 0, index);
            String[] selectRootArr = Arrays.copyOfRange(result, index, baseArr.length);
            result = CommonUtil.concatArray(selectRootArr, addArr);
        }

        return result;
    }

    // 선택한 rootScale과 calScale의 interval만큼 배열 다시 생성
    public static String[] selectScale(String rootScale, String calScale) throws Exception {
        String[] baseArr = sortScale(rootScale);
        String[] result = baseArr;

        if(baseArr.length > 0) {
            int index = Arrays.asList(baseArr).indexOf(rootScale);
            int interval = calculatInterval(rootScale, calScale);
            result = Arrays.copyOfRange(result, index, interval);
        }

        return result;
    }

    // 반음 확인
    public static int checkSemitone(String[] arr) {
        int result = 0;

        if(Arrays.asList(arr).containsAll(Arrays.asList(Constant.SEMITONE.MI_FA))) result += 1;
        if(Arrays.asList(arr).containsAll(Arrays.asList(Constant.SEMITONE.SI_DO))) result += 1;

        System.out.println("MI_FA : " + Arrays.asList(arr).containsAll(Arrays.asList(Constant.SEMITONE.MI_FA)));
        System.out.println("SI_DO : " + Arrays.asList(arr).containsAll(Arrays.asList(Constant.SEMITONE.SI_DO)));

        return result;
    }

    // 도수 확인
    public static String checkInterval(String rootScale, String calScale) throws Exception {
        String result = "";

        String[] arr = selectScale(rootScale, calScale);
        int interval = calculatInterval(rootScale, calScale);

        if(arr != null && arr.length > 0) {
            int semitoneCnt = checkSemitone(arr);
            System.out.println("interval : " + interval);
            switch (interval) {
                case 2 :
                    if(semitoneCnt > 0) {
                        result = Constant.INTERVAL.MINOR;
                    } else {
                        result = Constant.INTERVAL.MAJOR;
                    }
                    break;
                case 3 :
                    if(semitoneCnt > 0) {
                        result = Constant.INTERVAL.MINOR;
                    } else {
                        result = Constant.INTERVAL.MAJOR;
                    }
                    break;
                case 4 :
                    if(semitoneCnt > 0) {
                        result = Constant.INTERVAL.PERFECT;
                    } else {
                        result = Constant.INTERVAL.AUGUMETED;
                    }
                    break;
                case 5 :
                    if(semitoneCnt > 1) {
                        result = Constant.INTERVAL.PERFECT;
                    } else if(semitoneCnt == 2) {
                        result = Constant.INTERVAL.DIMINISHED;
                    }
                    break;
                case 6 :
                    if(semitoneCnt > 1) {
                        result = Constant.INTERVAL.MAJOR;
                    } else if(semitoneCnt == 2) {
                        result = Constant.INTERVAL.MINOR;
                    }
                    break;
                case 7 :
                    if(semitoneCnt > 1) {
                        result = Constant.INTERVAL.MAJOR;
                    } else if(semitoneCnt == 2) {
                        result = Constant.INTERVAL.MINOR;
                    }
                    break;
                case 8 :
                    result = Constant.INTERVAL.PERFECT;
                    break;
            }
        }
        return interval + " " + result;
    }
}
