package io.xenn.demo;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

import io.xenn.android.Xennio;
import io.xenn.hmskit.HmsKitPlugin;

public class MyHmsMessagingService extends HmsMessageService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        HmsKitPlugin hmsKitPlugin = Xennio.plugins().get(HmsKitPlugin.class);
        if(hmsKitPlugin.isXennioNotification(remoteMessage)){
            hmsKitPlugin.handlePushNotification(this, remoteMessage);
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Xennio.plugins().get(HmsKitPlugin.class).savePushToken(s);
    }
}