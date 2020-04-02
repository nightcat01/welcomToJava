package com.example.demo.common;

public class Constant {
    // 기호
    public static String SHARP  = "#";
    public static String FLAT   = "b";
//    public static String MAJOR   = "M";
//    public static String MINOR   = "m";
    public static String[] SCALE_ARR = {"C", "D", "E", "F", "G", "A", "B"};

    public static class SEMITONE {
        public static String[] MI_FA = {"E", "F"};
        public static String[] SI_DO = {"B", "C"};
    }

    public static class INTERVAL {
        public static String PERFECT = "Perfect Interval";
        public static String MAJOR = "Major Interval";
        public static String MINOR = "Minor Interval";
        public static String AUGUMETED = "Augumented Interval";
        public static String DIMINISHED = "Diminished Interval";
    }
}
