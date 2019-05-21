package io.xenn.demo;


import com.google.firebase.messaging.FirebaseMessagingService;

import io.xenn.android.XennioAPI;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        XennioAPI.savePushToken("", token);
    }
}
