package io.xenn.android.http;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.xenn.android.utils.XennioLogger;

public class PostFormUrlEncodedTask extends AsyncTask<Void, Void, Integer> {

    private final String payload;
    private final String endpoint;

    public PostFormUrlEncodedTask(String endpoint, String payload) {
        this.payload = payload;
        this.endpoint = endpoint;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            URL url = new URL(endpoint);
            byte[] postData = payload.getBytes();
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
            XennioLogger.log("Xenn API request completed with status code:" + responseCode);
            return responseCode;
        } catch (Exception e) {
            XennioLogger.log("Xenn API request failed" + e.getMessage());
        }
        return 0;
    }

    public String getPayload() {
        return payload;
    }

    public String getEndpoint() {
        return endpoint;
    }

}
