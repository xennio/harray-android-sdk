package io.xenn.hmskit.notification;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;

import java.util.Map;

import io.xenn.android.service.DeviceService;
import io.xenn.android.service.HttpService;
import io.xenn.android.utils.XennioLogger;
import io.xenn.hmskit.common.Constants;

public class NotificationCompatBuilder {

    private Context applicationContext;
    private final HttpService httpService;
    private NotificationCompat.Builder notificationCompat;
    private final DeviceService deviceService;


    public NotificationCompatBuilder(Context applicationContext, HttpService httpService, DeviceService deviceService) {
        this.applicationContext = applicationContext;
        this.httpService = httpService;
        this.deviceService = deviceService;
    }

    public static NotificationCompatBuilder create(Context applicationContext, HttpService httpService, DeviceService deviceService) {
        NotificationCompatBuilder notificationCompatBuilder = new NotificationCompatBuilder(applicationContext, httpService, deviceService);
        notificationCompatBuilder.applicationContext = applicationContext;
        return notificationCompatBuilder;
    }

    public NotificationCompatBuilder withChannelId(String notificationChannelId) {

        int appIconResId = -1;
        try {
            PackageManager packageManager = applicationContext.getPackageManager();
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(applicationContext.getPackageName(), PackageManager.GET_META_DATA);
            appIconResId = applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            XennioLogger.log(e.getMessage());
        }
        notificationCompat = new NotificationCompat.Builder(applicationContext, notificationChannelId)
                .setAutoCancel(true)
                .setVibrate(new long[]{500, 1000})
                .setSmallIcon(appIconResId);
        return this;
    }

    public NotificationCompatBuilder withTitle(String title) {
        notificationCompat.setContentTitle(title);
        return this;
    }

    public NotificationCompatBuilder withSubtitle(String subTitle) {
        notificationCompat.setContentText(subTitle);
        return this;
    }

    public NotificationCompatBuilder withMessage(String message) {
        if (message != null) {
            notificationCompat.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        }
        return this;
    }

    public NotificationCompatBuilder withBadge(int badge) {
        if (badge != 0) {
            notificationCompat.setNumber(badge);
        }
        return this;
    }

    public NotificationCompatBuilder withSound(String sound) {
        if (sound != null) {
            notificationCompat.setSound(deviceService.getSound(sound));
        } else {
            notificationCompat.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        return this;
    }

    public NotificationCompatBuilder withImage(String imageUrl, String message) {
        if (imageUrl != null) {
            Bitmap bitmap = httpService.downloadImage(imageUrl);
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle().bigPicture(bitmap);
            if (message != null) {
                bigPictureStyle.setSummaryText(message);
            }
            notificationCompat.setStyle(bigPictureStyle);
        }
        return this;
    }

    public NotificationCompatBuilder withIntent(Map<String, String> intentData) {
        PackageManager packageManager = applicationContext.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(applicationContext.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent notificationIntent = Intent.makeRestartActivityTask(componentName);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra("via", Constants.PUSH_CHANNEL_ID);
        for (Map.Entry<String, String> entry : intentData.entrySet()) {
            notificationIntent.putExtra(entry.getKey(), entry.getValue());
        }
        PendingIntent contentIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationCompat.setContentIntent(contentIntent);
        return this;
    }


    public NotificationCompat.Builder build() {
        return notificationCompat;
    }


    public NotificationCompatBuilder withApplicationLogo(String logo) {
        if (logo != null) {
            notificationCompat.setLargeIcon(httpService.downloadImage(logo));
        }
        return this;
    }
}
