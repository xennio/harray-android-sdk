package io.xenn.android.context;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;

import io.xenn.android.Xennio;
import io.xenn.android.utils.XennioLogger;

public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {

    @Nullable
    private static Activity currentActivity;

    @Nullable
    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        XennioLogger.log("onActivityCreated:" + activity.getLocalClassName());
        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        XennioLogger.log("onActivityStarted:" + activity.getLocalClassName());
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        XennioLogger.log("onActivityResumed:" + activity.getLocalClassName());
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        XennioLogger.log("onActivityPaused:" + activity.getLocalClassName());
        if (currentActivity == activity) {
            currentActivity = null;
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        XennioLogger.log("onActivityStopped:" + activity.getLocalClassName());
        if (currentActivity == activity) {
            currentActivity = null;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        XennioLogger.log("onActivityDestroyed:" + activity.getLocalClassName());
        if (currentActivity == activity) {
            currentActivity = null;
        }
    }
}
