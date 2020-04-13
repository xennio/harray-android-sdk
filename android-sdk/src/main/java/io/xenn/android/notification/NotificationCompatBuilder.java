package io.xenn.android.notification;

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

import io.xenn.android.common.DeviceUtils;
import io.xenn.android.utils.ImageDownloadManager;
import io.xenn.android.utils.XennioLogger;

public class NotificationCompatBuilder {

    private Context applicationContext;
    private NotificationCompat.Builder notificationCompat;

    public NotificationCompatBuilder(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static NotificationCompatBuilder create(Context applicationContext) {
        NotificationCompatBuilder notificationCompatBuilder = new NotificationCompatBuilder(applicationContext);
        notificationCompatBuilder.applicationContext = applicationContext;
        return notificationCompatBuilder;
    }

    public NotificationCompatBuilder withChannelId(String notificationChannelId) {

        int appIconResId = 0;
        try {
            PackageManager packageManager = applicationContext.getPackageManager();
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(applicationContext.getPackageName(), PackageManager.GET_META_DATA);
            appIconResId = applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            XennioLogger.log(e.getMessage());
        }
        notificationCompat = new NotificationCompat.Builder(applicationContext, notificationChannelId)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSmallIcon(appIconResId)
                .setAutoCancel(true);
        return this;
    }

    public NotificationCompatBuilder withTitle(String title) {
        notificationCompat.setContentTitle(title != null ? title : DeviceUtils.getAppLabel(applicationContext, ""));
        return this;
    }

    public NotificationCompatBuilder withMessage(String message) {
        notificationCompat.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
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
            notificationCompat.setSound(DeviceUtils.getSound(applicationContext, sound));
        } else {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        return this;
    }

    public NotificationCompatBuilder withImage(String imageUrl, String message) {
        if (imageUrl != null) {
            Bitmap bitmap = ImageDownloadManager.getInstance().getBitmap(imageUrl);
            notificationCompat.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(message));
        }
        return this;
    }

    public NotificationCompatBuilder withIntent(Map<String, String> intentData) {
        PackageManager packageManager = applicationContext.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(applicationContext.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent notificationIntent = Intent.makeRestartActivityTask(componentName);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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


}
