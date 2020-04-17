package io.xenn.android.utils;

import java.util.UUID;

public class RandomValueUtils {

    private static boolean isFrozen = false;
    private static String randomUUIDValue;


    public static String randomUUID() {
        if (isFrozen) {
            return randomUUIDValue;
        }
        return UUID.randomUUID().toString();
    }

    public static void freeze() {
        isFrozen = true;
        randomUUIDValue = UUID.randomUUID().toString();
    }

    public static void unFreeze() {
        randomUUIDValue = null;
        isFrozen = false;
    }
}
