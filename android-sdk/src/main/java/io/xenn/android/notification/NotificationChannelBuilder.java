package io.xenn.android.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import io.xenn.android.common.Constants;
import io.xenn.android.common.DeviceUtils;

public class NotificationChannelBuilder {

    private NotificationChannel notificationChannel;
    private Context applicationContext;

    public NotificationChannelBuilder(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static NotificationChannelBuilder create(Context applicationContext) {
        NotificationChannelBuilder notificationChannelBuilder = new NotificationChannelBuilder(applicationContext);
        return notificationChannelBuilder;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createIn(NotificationManager notificationManager) {
        notificationManager.createNotificationChannel(notificationChannel);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public NotificationChannelBuilder withChannelId(String channelId) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        notificationChannel = new NotificationChannel(channelId, Constants.PUSH_CHANNEL_NAME, importance);
        notificationChannel.setDescription(Constants.PUSH_CHANNEL_DESCRIPTION);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        return this;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public NotificationChannelBuilder withImportance(int importance) {
        notificationChannel.setImportance(importance);
        return this;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public NotificationChannelBuilder withSound(String sound) {
        if (sound != null) {
            Uri soundUri = DeviceUtils.getSound(applicationContext, sound);
            AudioAttributes attributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
            notificationChannel.setSound(soundUri, attributes);
        }
        return this;
    }

}
