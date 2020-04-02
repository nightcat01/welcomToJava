package com.example.demo.common;

public enum Scale {

//    DO("C", 1),
//    RE("D", 3),
//    MI("E", 5),
//    FA("F", 6),
//    SOL("G", 8),
//    LA("A", 10),
//    SI("B", 12);

    DO("C", 1),
    RE("D", 2),
    MI("E", 3),
    FA("F", 4),
    SOL("G", 5),
    LA("A", 6),
    SI("B", 7);

    private String scale;
    private int value;

    Scale(String scale, int value) {
        this.scale = scale;
        this.value = value;
    }

    public String getScale() {
        return this.scale;
    }

    public int getValue() {
        return this.value;
    }

//    @Override
//    public String toString() {
//        return super.toString() + "(CODE : " + this.scale + ", value : " + Integer.toString(this.value) + ")";
//    }
}
