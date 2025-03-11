package com.distraction.oss325;

public class Utils {

    public static int roundNearestFloor(float number, int n) {
        return (int) (Math.floor((double) number / n) * n);
    }

}
