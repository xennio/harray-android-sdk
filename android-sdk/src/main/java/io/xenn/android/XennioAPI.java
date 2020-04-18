package io.xenn.android;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import android.util.Base64;

import com.google.firebase.messaging.RemoteMessage;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.xenn.android.common.Constants;
import io.xenn.android.common.DeviceUtils;
import io.xenn.android.common.PushMessageDataWrapper;
import io.xenn.android.model.XennEvent;
import io.xenn.android.notification.NotificationChannelBuilder;
import io.xenn.android.notification.NotificationCompatBuilder;
import io.xenn.android.utils.ClockUtils;
import io.xenn.android.utils.XennioLogger;

@Deprecated
public class XennioAPI {

    private static final String COLLECTOR_URL = "https://c.xenn.io:443/";
    private static final List<String> deepLinkKeys = Collections.unmodifiableList(Arrays.asList("campaignId", "campaignDate", "pushId", "url", "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content"));
    private static String collectorUrl;
    private static String pid;
    private static String sid;
    private static Map<String, Object> deepLink = new HashMap<>();
    private static final Long HEART_BEAT_INTERVAL = 55 * 1000L;
    private static final long EXPIRE_TIME = 30 * 60 * 1000L;
    private static long lastEventTime = ClockUtils.getTime();
    private static TimerTask heartBeatTask;
    private static Timer timer = new Timer();
    private static boolean isInitialized = false;
    private static String appVersion;
    private static String carrier;
    private static String sdkVersion = "1.1";


    private XennioAPI() {
    }

    public static void init(Context context, String sdkKey) {
        lastEventTime = System.currentTimeMillis();
        XennioAPI.sid = UUID.randomUUID().toString();
        XennioAPI.pid = getOrCreatePersistentId(context);
        XennioAPI.carrier = DeviceUtils.carrier(context);
        XennioAPI.appVersion = DeviceUtils.appVersion(context);
        XennioAPI.collectorUrl = COLLECTOR_URL + sdkKey;
        XennioAPI.isInitialized = true;
        XennioLogger.log("Xenn.io SDK initialized with " + sdkKey);
        sessionStart();
    }

    private static void scheduleTimer() {
        timer.purge();
        timer.cancel();
        timer = new Timer();
        heartBeatTask = new TimerTask() {
            @Override
            public void run() {
                XennioAPI.hearthBeat();
            }
        };
        timer.schedule(heartBeatTask, HEART_BEAT_INTERVAL);
    }

    private static void sessionStart() {
        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String timeZone = Long.toString(TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS));
        XennEvent xennEvent = XennEvent.create("SS")
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addHeader("sv", sdkVersion)
                .addBody("os", "Android " + DeviceUtils.osVersion())
                .addBody("md", DeviceUtils.model())
                .addBody("mn", DeviceUtils.manufacturer())
                .addBody("br", DeviceUtils.brand())
                .addBody("id", DeviceUtils.getDeviceUniqueId())
                .addBody("op", carrier)
                .addBody("av", appVersion)
                .addBody("zn", timeZone)
                .appendExtra(deepLink);

        post(xennEvent);
    }

    public static void hearthBeat() {
        XennEvent xennEvent = XennEvent.create("HB")
                .addHeader("s", getSid())
                .addHeader("p", pid);
        post(xennEvent);
    }

    public static void pageView(String memberId, String pageType, Map<String, Object> params) {
        XennEvent xennEvent = XennEvent.create("PV")
                .memberId(memberId)
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("pageType", pageType)
                .appendExtra(params)
                .appendExtra(deepLink);

        post(xennEvent);
    }

    public static void impression(String memberId, String type, Map<String, Object> params) {
        XennEvent xennEvent = XennEvent.create("IM")
                .memberId(memberId)
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("type", type)
                .appendExtra(params);
        post(xennEvent);
    }

    private static String getSid() {
        if (lastEventTime + EXPIRE_TIME < System.currentTimeMillis()) {
            sid = UUID.randomUUID().toString();
            deepLink = null;
            XennioLogger.log("Xenn.io Session expired new session id will be created");
        }
        return sid;
    }

    public static void actionResult(String memberId, String type, Map<String, Object> params) {
        XennEvent xennEvent = XennEvent
                .create("AR")
                .memberId(memberId)
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("type", type)
                .appendExtra(params);

        post(xennEvent);
    }

    public static void savePushToken(String memberId, String deviceToken) {
        XennEvent xennEvent = XennEvent.create("Collection")
                .memberId(memberId)
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("name", "pushToken")
                .addBody("type", "fcmToken")
                .addBody("appType", "fcmAppPush")
                .addBody("deviceToken", deviceToken);

        post(xennEvent);
    }

    private static void putPushDeepLink(Map<String, String> data) {
        for (Map.Entry<String, String> each : data.entrySet()) {
            deepLink.put(each.getKey(), each.getValue());
        }
    }

    public static String getDeepLink(String key) {
        return deepLink.containsKey(key) ? deepLink.get(key).toString() : null;
    }

    public static void pushReceived() {
        XennEvent xennEvent = XennEvent.create("Feedback")
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("type", "pushReceived")
                .appendExtra(deepLink);

        post(xennEvent);
    }

    public static void pushOpened(Intent intent) {
        XennEvent xennEvent = XennEvent.create("Feedback")
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("type", "pushOpened")
                .appendExtra(deepLink)
                .appendExtra(deeplinkExtrasFrom(intent));

        post(xennEvent);
    }

    public static void handlePushOpen(Intent intent, List<String> pushProviders) {
        if (intent != null && pushProviders.contains(intent.getStringExtra(Constants.PUSH_PAYLOAD_SOURCE))) {
            XennioAPI.pushOpened(intent);
        }
    }

    public static void handlePushNotification(Context applicationContext, RemoteMessage remoteMessage) {
        try {
            Map<String, String> data = remoteMessage.getData();
            PushMessageDataWrapper pushMessageDataWrapper = PushMessageDataWrapper.from(data);
            XennioAPI.putPushDeepLink(data);
            XennioAPI.pushReceived();

            if (pushMessageDataWrapper.isSilent()) {
                XennioAPI.pushOpened(null);
                return;
            }

            String notificationChannelId = pushMessageDataWrapper.buildChannelId();
            NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
                NotificationChannelBuilder.create(applicationContext).withChannelId(notificationChannelId).withSound(pushMessageDataWrapper.getSound()).createIn(notificationManager);
            }
            NotificationCompat.Builder notificationCompatBuilder = NotificationCompatBuilder.create(applicationContext)
                    .withChannelId(notificationChannelId)
                    .withTitle(pushMessageDataWrapper.getTitle())
                    .withMessage(pushMessageDataWrapper.getMessage())
                    .withBadge(pushMessageDataWrapper.getBadge())
                    .withSound(pushMessageDataWrapper.getSound())
                    .withImage(pushMessageDataWrapper.getImageUrl(), pushMessageDataWrapper.getMessage())
                    .withIntent(data)
                    .build();

            notificationManager.notify(12, notificationCompatBuilder.build());
        } catch (Exception e) {
            XennioLogger.log("Xenn Push handle error:" + e.getMessage());
        }
    }

    private static Map<String, Object> deeplinkExtrasFrom(Intent intent) {
        if (intent == null) {
            return null;
        }

        Map<String, Object> extras = new HashMap<>();
        for (String key : deepLinkKeys) {
            if (intent.hasExtra(key)) {
                extras.put(key, intent.getStringExtra(key));
            }
        }
        return extras;
    }

    private static String getOrCreatePersistentId(Context context) {
        SharedPreferences xennio_prefs = context.getSharedPreferences("XENNIO_PREFS", Context.MODE_PRIVATE);
        String value = xennio_prefs.getString(Constants.SDK_PERSISTENT_ID_KEY, null);
        if (value == null) {
            value = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = xennio_prefs.edit();
            editor.putString(Constants.SDK_PERSISTENT_ID_KEY, value);
            editor.apply();
        }
        return value;
    }

    private static void post(final XennEvent xennEvent) {
        if (isInitialized) {
            scheduleTimer();
            lastEventTime = System.currentTimeMillis();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        String jsonXennEvent = xennEvent.toJSON().toString();
                        String urlEncodedXennEvent = URLEncoder.encode(jsonXennEvent, "UTF-8");
                        String encodedEvent = Base64.encodeToString(urlEncodedXennEvent.getBytes(), Base64.DEFAULT);

                        String payload = "e=" + encodedEvent;
                        byte[] postData = payload.getBytes();

                        URL url = new URL(collectorUrl);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setDoOutput(true);
                        urlConnection.setInstanceFollowRedirects(false);
                        urlConnection.setRequestMethod("POST");
                        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));
                        urlConnection.setUseCaches(false);

                        DataOutputStream dStream = new DataOutputStream(urlConnection.getOutputStream());
                        dStream.write(postData);
                        dStream.flush();
                        dStream.close();
                        int responseCode = urlConnection.getResponseCode();
                        XennioLogger.log("Xenn API " + xennEvent.getName() + " request completed with status code:" + responseCode);
                    } catch (Exception e) {
                        XennioLogger.log("Xenn API " + xennEvent.getName() + " request failed" + e.getMessage());
                    }
                    return null;

                }
            }.execute();
        } else {
            XennioLogger.log("Xenn.io SDK not initialized yet. Call XennApi.init method before sending events.");
        }

    }

}
