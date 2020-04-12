package io.xenn.android.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import io.xenn.android.common.Constants;

public class XennioLogger {
    public static void debugLog(@NonNull String message) {
        Log.d(Constants.LOG_TAG, message);
    }
}
