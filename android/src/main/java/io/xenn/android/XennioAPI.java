package io.xenn.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

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

    private static String collectorUrl;
    private static String pid;
    private static String sid;

    private XennioAPI() {
    }

    public static void init(Context context, String sdkKey) {
        XennioAPI.sid = UUID.randomUUID().toString();
        XennioAPI.pid = getSharedPrefValue("pid", context);
        XennioAPI.collectorUrl = "https://c.xenn.io:443" + "/" + sdkKey;
        Log.d("Xennio", "Xenn.io SDK intialized with " + sdkKey);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                XennioAPI.hearthBeat();
            }
        }, 55 * 1000L);
    }

    public static void sessionStart(String memberId, Map<String, Object> params) {
        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        String timeZone = Long.toString(TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS));
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("SS")
                .memberId(memberId)
                .addHeader("s", sid)
                .addHeader("p", pid)
                .addBody("os", "Android " + Build.VERSION.RELEASE)
                .addBody("md", Build.MODEL)
                .addBody("mn", Build.MANUFACTURER)
                .addBody("br", Build.BRAND)
                .addBody("id", getDeviceUniqueId())
                .addBody("zn", timeZone)
                .appendExtra(params);
        post(xennEvent);
    }

    public static void hearthBeat() {
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("HB")
                .addHeader("s", sid)
                .addHeader("p", pid);

        post(xennEvent);
    }

    public static void pageView(String memberId, String pageType, Map<String, Object> params) {
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("PV")
                .memberId(memberId)
                .addHeader("s", sid)
                .addHeader("p", pid)
                .addBody("pageType", pageType)
                .appendExtra(params);

        post(xennEvent);
    }

    public static void actionResult(String memberId, String type, Map<String, Object> params) {
        XennEvent xennEvent = new XennEvent();
        xennEvent
                .name("AR")
                .memberId(memberId)
                .addHeader("s", sid)
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
                .addHeader("s", sid)
                .addHeader("p", pid)
                .addBody("name", "pushToken")
                .addBody("type", "fcmToken")
                .addBody("appType", "fcmAppPush")
                .addBody("deviceToken", deviceToken);

        post(xennEvent);
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
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    String jsonXennEvent = new JSONObject(xennEvent.toMap()).toString();
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
    }

    private static String getDeviceUniqueId() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial"; // some value
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
