package io.xenn.android.utils;

public class ClockUtils {

    private static boolean isFrozen = false;
    private static long currentTime;

    public static long getTime() {
        if (isFrozen) {
            return currentTime;
        }
        return System.currentTimeMillis();
    }

    public static void freeze() {
        isFrozen = true;
        currentTime = System.currentTimeMillis();
    }

    public static void freeze(long expectedTime) {
        isFrozen = true;
        currentTime = expectedTime;
    }

    public static void unFreeze() {
        currentTime = 0;
        isFrozen = false;
    }
}
