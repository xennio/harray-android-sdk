package io.xenn.demo;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import io.xenn.android.XennioAPI;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        XennioAPI.savePushToken("", token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        XennioAPI.putPushDeeplink(remoteMessage.getData());
        XennioAPI.pushReceived();
        super.onMessageReceived(remoteMessage);
    }
}
