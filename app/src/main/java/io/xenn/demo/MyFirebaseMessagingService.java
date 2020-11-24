package io.xenn.demo;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.xenn.android.Xennio;
import io.xenn.fcmkit.FcmKitPlugin;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Xennio.plugins().get(FcmKitPlugin.class).handlePushNotification(this, remoteMessage);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Xennio.plugins().get(FcmKitPlugin.class).savePushToken(s);
    }
}
