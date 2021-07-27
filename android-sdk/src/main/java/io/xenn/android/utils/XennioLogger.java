package io.xenn.android.utils;

import android.util.Log;

import io.xenn.android.common.Constants;

public class XennioLogger {
    public static void log(String message) {
        Log.d(Constants.LOG_TAG, message);
    }

    public static void log(String message, Throwable throwable) {
        Log.d(Constants.LOG_TAG, message, throwable);
    }
}
