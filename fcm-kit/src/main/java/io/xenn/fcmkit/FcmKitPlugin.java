package io.xenn.fcmkit;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.RemoteMessage;

import io.xenn.android.context.XennPlugin;
import io.xenn.fcmkit.common.Constants;
import io.xenn.fcmkit.notification.NotificationProcessorHandler;

import static io.xenn.android.Xennio.getApplicationContextHolder;
import static io.xenn.android.Xennio.getDeviceService;
import static io.xenn.android.Xennio.getEntitySerializerService;
import static io.xenn.android.Xennio.getHttpService;
import static io.xenn.android.Xennio.getSessionContextHolder;

public class FcmKitPlugin extends XennPlugin {

    private NotificationProcessorHandler notificationProcessorHandler;
    private String pushNotificationToken = "";

    public FcmKitPlugin() {
        this.notificationProcessorHandler = new NotificationProcessorHandler(
                getApplicationContextHolder(),
                getSessionContextHolder(),
                getHttpService(),
                getEntitySerializerService(),
                getDeviceService()
        );
    }

    @Override
    public void onCreate(Context context) {
        resetBadgeCounts(context);
    }

    @Override
    public void onLogin() {
        if (!"".equals(pushNotificationToken)) {
            this.savePushToken(pushNotificationToken);
        }
    }

    @Override
    public void onLogout() {
        removeTokenAssociation(this.pushNotificationToken);
    }

    public void savePushToken(String deviceToken) {
        this.pushNotificationToken = deviceToken;
        notificationProcessorHandler.savePushToken(deviceToken);
    }

    public void removeTokenAssociation(String deviceToken) {
        this.pushNotificationToken = "";
        notificationProcessorHandler.removeTokenAssociation(deviceToken);
    }


    public boolean isXennioNotification(RemoteMessage remoteMessage) {
        return remoteMessage.getData().get(Constants.PUSH_PAYLOAD_SOURCE).equals(Constants.PUSH_CHANNEL_ID);
    }

    public void handlePushNotification(Context applicationContext, RemoteMessage remoteMessage) {
        notificationProcessorHandler.handlePushNotification(applicationContext, remoteMessage);
    }

    public void pushMessageOpened(Intent intent) {
        notificationProcessorHandler.pushMessageOpened(intent);
    }

    public void resetBadgeCounts(Context applicationContext) {
        notificationProcessorHandler.resetBadgeCounts(applicationContext);
    }
}
