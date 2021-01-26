package io.xenn.android.service;

import android.graphics.Bitmap;

import java.util.Map;

import io.xenn.android.common.ResultConsumer;
import io.xenn.android.common.ResponseBodyHandler;
import io.xenn.android.http.BitmapDownloadTask;
import io.xenn.android.http.HttpRequestFactory;
import io.xenn.android.http.PostFormUrlEncodedTask;
import io.xenn.android.http.PostJsonEncodedTask;

public class HttpService {

    private static final String collectorUrl = "https://c.xenn.io:443/";
    private static final String apiUrl = "https://api.xenn.io:443";
    private final String sdkKey;
    private final HttpRequestFactory httpRequestFactory;

    public HttpService(HttpRequestFactory httpRequestFactory, String sdkKey) {
        this.httpRequestFactory = httpRequestFactory;
        this.sdkKey = sdkKey;
    }

    public <T> void getApiRequest(String path,
                                  Map<String, String> params,
                                  ResponseBodyHandler<T> rh,
                                  ResultConsumer<T> callback) {
        httpRequestFactory
                .getHttpGetTask(getApiUrl(path, params), rh, callback)
                .execute();
    }

    public void postFormUrlEncoded(final String payload) {
        PostFormUrlEncodedTask task = httpRequestFactory.getPostFormUrlEncodedTask(getCollectorUrl(), "e=" + payload);
        task.execute();
    }

    public Bitmap downloadImage(String endpoint) {
        BitmapDownloadTask bitmapDownloadTask = httpRequestFactory.getBitmapDownloadTask(endpoint);
        return bitmapDownloadTask.getBitmap();
    }

    public void postJsonEncoded(final String payload, final String path) {
        PostJsonEncodedTask postJsonEncodedTask = httpRequestFactory.getPostJsonEncodedTask(getCollectorUrl(path), payload);
        postJsonEncodedTask.execute();
    }

    public String getCollectorUrl() {
        return collectorUrl + sdkKey;
    }

    public String getCollectorUrl(String feedback) {
        return collectorUrl + feedback;
    }

    private String getApiUrl(String path, Map<String, String> params) {
        StringBuilder paramsAsStr = new StringBuilder("?");
        for (Map.Entry<String, String> paramEntry : params.entrySet()) {
            paramsAsStr
                    .append(paramEntry.getKey())
                    .append("=")
                    .append(paramEntry.getValue())
                    .append("&");
        }
        return apiUrl + path + paramsAsStr.toString();
    }
}
