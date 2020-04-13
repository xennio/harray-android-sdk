package io.xenn.demo;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.xenn.android.XennioAPI;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        XennioAPI.handlePushNotification(this, remoteMessage);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        XennioAPI.savePushToken("", s);
    }
}
