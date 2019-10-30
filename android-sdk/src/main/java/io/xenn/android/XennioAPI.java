package io.xenn.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.xenn.android.model.XennEvent;

public class XennioAPI {

    private static final String COLLECTOR_URL = "https://c.xenn.io:443/";
    private static String collectorUrl;
    private static String pid;
    private static String sid;
    private static String deeplinkUrl;
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
        Log.d("Xennio", "Xenn.io SDK intialized with " + sdkKey);
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

        if (XennioAPI.deeplinkUrl != null && !"".equalsIgnoreCase(XennioAPI.deeplinkUrl)) {
            xennEvent.addBody("deeplink", XennioAPI.deeplinkUrl);
        }

        post(xennEvent);
    }

    public static void impression (String memberId, String pageType, Map<String, Object> params) {
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("IM")
                .memberId(memberId)
                .addHeader("s", getSid())
                .addHeader("p", pid)
                .addBody("pageType", pageType)
                .appendExtra(params);
        post(xennEvent);
    }

    private static String getSid() {
        if (lastEventTime + EXPIRE_TIME < System.currentTimeMillis()) {
            sid = UUID.randomUUID().toString();
            deeplinkUrl = null;
            Log.d("Xennio", "Session expired new session id will be created");
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

    public static void putDeeplinkURL(String deeplinkUrl) {
        XennioAPI.deeplinkUrl = deeplinkUrl;
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
                        Log.d("Xennio", "Server request completed with status code:" + responseCode);
                    } catch (Exception e) {
                        Log.e("Xennio", "Error occurred" + e.getMessage());
                    }
                    return null;

                }
            }.execute();
        } else {
            Log.d("Xennio", "Xenn.io SDK not initialized yet. Call XennApi.init method before sending events.");
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
