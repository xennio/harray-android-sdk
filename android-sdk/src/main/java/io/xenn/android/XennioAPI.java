package io.xenn.android;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
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
import io.xenn.android.common.PushMessageDataWrapper;
import io.xenn.android.model.XennEvent;
import io.xenn.android.notification.NotificationChannelBuilder;
import io.xenn.android.notification.NotificationCompatBuilder;
import io.xenn.android.utils.XennioLogger;

public class XennioAPI {

    private static final String COLLECTOR_URL = "https://c.xenn.io:443/";
    private static final List<String> deeplinkKeys = Collections.unmodifiableList(Arrays.asList("campaignId", "campaignDate", "pushId", "url", "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content"));
    private static String collectorUrl;
    private static String pid;
    private static String sid;
    private static Map<String, Object> deeplink;
    private static final Long HEART_BEAT_INTERVAL = 55 * 1000L;
    private static final Long EXPIRE_TIME = 30 * 60 * 1000L;
    private static Long lastEventTime;
    private static TimerTask heartBeatTask;
    private static Timer timer = new Timer();
    private static boolean isInitialized = false;


    private XennioAPI() {
    }

    public static void init(Context context, String sdkKey) {
        lastEventTime = System.currentTimeMillis();
        XennioAPI.sid = UUID.randomUUID().toString();
        XennioAPI.pid = getSharedPrefValue("pid", context);
        XennioAPI.collectorUrl = COLLECTOR_URL + sdkKey;
        XennioAPI.isInitialized = true;
        XennioLogger.debugLog("Xenn.io SDK initialized with " + sdkKey);
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
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("SS")
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("os", "Android " + Build.VERSION.RELEASE)
                .addBody("md", Build.MODEL)
                .addBody("mn", Build.MANUFACTURER)
                .addBody("br", Build.BRAND)
                .addBody("id", getDeviceUniqueId())
                .addBody("zn", timeZone);
        post(xennEvent);
    }

    public static void hearthBeat() {
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("HB")
                .addHeader("s", getSid())
                .addHeader("p", pid);

        post(xennEvent);
    }

    public static void pageView(String memberId, String pageType, Map<String, Object> params) {
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("PV")
                .memberId(memberId)
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("pageType", pageType)
                .appendExtra(params);

        if (deeplink != null && !deeplink.isEmpty()) {
            xennEvent.appendExtra(deeplink);
        }
        post(xennEvent);
    }

    public static void impression(String memberId, String type, Map<String, Object> params) {
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("IM")
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
            deeplink = null;
            XennioLogger.debugLog("Xenn.io Session expired new session id will be created");
        }
        return sid;
    }

    public static void actionResult(String memberId, String type, Map<String, Object> params) {
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("AR")
                .memberId(memberId)
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("type", type)
                .appendExtra(params);

        post(xennEvent);
    }

    public static void savePushToken(String memberId, String deviceToken) {
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("Collection")
                .memberId(memberId)
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("name", "pushToken")
                .addBody("type", "fcmToken")
                .addBody("appType", "fcmAppPush")
                .addBody("deviceToken", deviceToken);

        post(xennEvent);
    }

    public static void putPushDeeplink(Map<String, String> data) {
        if (deeplink == null) {
            deeplink = new HashMap<>();
        }

        for (String key : deeplinkKeys) {
            if (data.containsKey(key)) {
                deeplink.put(key, data.get(key));
            }
        }
    }

    public static void pushReceived() {
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("Feedback")
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("type", "pushReceived")
                .appendExtra(deeplink);

        post(xennEvent);
    }

    public static void pushOpened(Intent intent) {
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("Feedback")
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("type", "pushOpened")
                .appendExtra(deeplink)
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
            XennioAPI.putPushDeeplink(data);
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
            XennioLogger.debugLog("Xenn Push handle error:" + e.getMessage());
        }
    }

    private static Map<String, Object> deeplinkExtrasFrom(Intent intent) {
        if (intent == null) {
            return null;
        }

        Map<String, Object> extras = new HashMap<>();
        for (String key : deeplinkKeys) {
            if (intent.hasExtra(key)) {
                extras.put(key, intent.getStringExtra(key));
            }
        }
        return extras;
    }

    private static String getSharedPrefValue(String key, Context context) {
        SharedPreferences xennio_prefs = context.getSharedPreferences("XENNIO_PREFS", Context.MODE_PRIVATE);
        String value = xennio_prefs.getString(key, null);
        if (value == null) {
            value = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = xennio_prefs.edit();
            editor.putString(key, value);
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
                        XennioLogger.debugLog("Xenn API " + xennEvent.getName() + " request completed with status code:" + responseCode);
                    } catch (Exception e) {
                        XennioLogger.debugLog("Xenn API " + xennEvent.getName() + " request failed" + e.getMessage());
                    }
                    return null;

                }
            }.execute();
        } else {
            XennioLogger.debugLog("Xenn.io SDK not initialized yet. Call XennApi.init method before sending events.");
        }

    }

    private static String getDeviceUniqueId() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial";
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
