package io.xenn.android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class XennioFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "xennio";
    private static final Set<String> blacklist =
            Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("source", "title", "message", "sound", "image_url")));

    private final String channelName;
    private final int importance;
    private final int notificationIcon;
    private final Class<?> intentClass;

    public XennioFirebaseMessagingService(Class<?> intentClass, String channelName, int notificationIcon, int importance) {
        this.intentClass = intentClass;
        this.channelName = channelName;
        this.notificationIcon = notificationIcon;
        this.importance = importance;
    }

    public XennioFirebaseMessagingService(Class<?> intentClass, int notificationIcon) {
        this(intentClass, CHANNEL_ID, notificationIcon, Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? NotificationManager.IMPORTANCE_DEFAULT : 3);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        XennioAPI.savePushToken(null, token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData() == null || remoteMessage.getData().isEmpty()) {
            return;
        }

        Map<String, String> data = remoteMessage.getData();
        if (!data.containsKey("source") || !data.get("source").equals("xenn")) { // Not xenn notification
            return;
        }

        XennioAPI.putPushDeeplink(data);
        XennioAPI.pushReceived();

        if (!data.containsKey("title")) { // Silent notification
            XennioAPI.pushOpened(null);
            return;
        }

        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(this.notificationIcon)
                .setContentTitle(data.get("title"))
                .setContentText(data.get("message"))
                .setPriority(this.importance - 2) // Different constants
                .setContentIntent(createPendingIntent(data))
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private PendingIntent createPendingIntent(Map<String, String> data) {
        Intent intent = new Intent(this, this.intentClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        for (String key : data.keySet()) {
            if (!blacklist.contains(key)) {
                intent.putExtra(key, data.get(key));
            }
        }
        intent.putExtra("isPushNotification", true);

        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, this.channelName, this.importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

}
