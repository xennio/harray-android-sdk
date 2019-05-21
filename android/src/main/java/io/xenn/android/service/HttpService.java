package io.xenn.android.service;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import io.xenn.android.model.XennEvent;

public class HttpService extends AsyncTask<XennEvent, Void, Void> {

    private final String sdkKey;
    private static final String XENN_IO_EVENT_COLLECTOR_URL = "https://c.xenn.io";

    public HttpService(String sdkKey) {
        this.sdkKey = sdkKey;
    }

    public void send(XennEvent xennEvent) {
        doInBackground(xennEvent);
    }

    @Override
    protected Void doInBackground(XennEvent... xennEvents) {
        XennEvent event = xennEvents[0];
        try {
            String jsonXennEvent = new JSONObject(event.toMap()).toString();
            String urlEncodedXennEvent = URLEncoder.encode(jsonXennEvent, "UTF-8");
            String encodedEvent = Base64.encodeToString(urlEncodedXennEvent.getBytes(), Base64.DEFAULT);

            byte[] postData = ("e=" + encodedEvent).getBytes();
            URL url = new URL(XENN_IO_EVENT_COLLECTOR_URL + "/" + sdkKey);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));
            urlConnection.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(postData);
            wr.flush();
            wr.close();
            int responseCode = urlConnection.getResponseCode();
            Log.d("Xennio", "Server request completed with status code:" + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Xennio", "Error occurred" + e.getMessage());
        }
        return null;
    }
}