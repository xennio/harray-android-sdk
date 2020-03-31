package io.xenn.demo;


import android.app.NotificationManager;

import io.xenn.android.XennioFirebaseMessagingService;

public class MyFirebaseMessagingService extends XennioFirebaseMessagingService {

    public MyFirebaseMessagingService() {
        super(MainActivity.class, "demo-app", R.drawable.ic_xenn_logo_white, NotificationManager.IMPORTANCE_HIGH);
    }
}
