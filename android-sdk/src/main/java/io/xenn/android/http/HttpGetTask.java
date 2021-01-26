package io.xenn.android.http;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

import io.xenn.android.common.ResultConsumer;
import io.xenn.android.common.ResponseBodyHandler;
import io.xenn.android.utils.IOUtils;
import io.xenn.android.utils.XennioLogger;

public class HttpGetTask<T> extends AsyncTask<Void, Void, T> {

    private final String endpoint;
    private final ResponseBodyHandler<T> rh;
    private final ResultConsumer<T> callback;

    public HttpGetTask(String endpoint,
                       ResponseBodyHandler<T> rh,
                       ResultConsumer<T> callback) {
        this.endpoint = endpoint;
        this.rh = rh;
        this.callback = callback;
    }

    @Override
    protected T doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(endpoint).openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            XennioLogger.log("Xenn API request completed with status code:" + urlConnection.getResponseCode());
            return rh.handle(IOUtils.readAll(urlConnection.getInputStream()));
        } catch (Exception e) {
            XennioLogger.log("Xenn API request failed" + e.getMessage());
            return null;
        } finally {
            disconnect(urlConnection);
        }
    }

    @Override
    protected void onPostExecute(T responseJson) {
        super.onPostExecute(responseJson);
        callback.consume(responseJson);
    }

    private void disconnect(HttpURLConnection urlConnection) {
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
    }
}
