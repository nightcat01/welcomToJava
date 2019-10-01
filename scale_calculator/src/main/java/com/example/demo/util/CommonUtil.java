package com.example.demo.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public class CommonUtil {

    // 배열
    public static <T> T[] concatArray(T[] a1, T[] a2 ) {

        // 배열이 아닐 경우 에러
        if (!a1.getClass().isArray() || !a2.getClass().isArray()) {
            throw new IllegalArgumentException();
        }

        int a1Len = a1.length;
        int a2Len = a2.length;

        T[] result = (T[]) Array.newInstance(a1.getClass().getComponentType(), a1Len + a2Len);
        // 배열 복사
        // a1[0]부터 배열 복사, result[0]부터 result[a1Len]까지
        System.arraycopy(a1, 0, result, 0, a1Len);
        System.arraycopy(a2, 0, result, a1Len, a2Len);

        return result;
    }

    // 배열
    // Integer, Double, 등 객체, 기본 배열에서 모두 동작
    public static <T> T concatGenericArray(T[] a1, T[] a2 ) {

        if (!a1.getClass().isArray() || !a2.getClass().isArray()) {
            throw new IllegalArgumentException();
        }

        Class<?> resCompType;
        Class<?> aCompType = a1.getClass().getComponentType();
        Class<?> bCompType = a2.getClass().getComponentType();

        if (aCompType.isAssignableFrom(bCompType)) {
            resCompType = aCompType;
        } else if (bCompType.isAssignableFrom(aCompType)) {
            resCompType = bCompType;
        } else {
            throw new IllegalArgumentException();
        }

        int a1Len = Array.getLength(a1);
        int a2Len = Array.getLength(a2);

        @SuppressWarnings("unchecked")
        T result = (T) Array.newInstance(resCompType, a1Len + a2Len);
        System.arraycopy(a1, 0, result, 0, a1Len);
        System.arraycopy(a2, 0, result, a1Len, a2Len);

        return result;
    }



}
