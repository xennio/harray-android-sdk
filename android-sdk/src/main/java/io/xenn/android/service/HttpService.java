package io.xenn.android.service;

import android.graphics.Bitmap;

import io.xenn.android.http.BitmapDownloadTask;
import io.xenn.android.http.HttpRequestFactory;
import io.xenn.android.http.PostFormUrlEncodedTask;
import io.xenn.android.http.PostJsonEncodedTask;

public class HttpService {

    private final String collectorUrl = "https://c.xenn.io:443/";
    private final String sdkKey;
    private final HttpRequestFactory httpRequestFactory;

    public HttpService(HttpRequestFactory httpRequestFactory, String sdkKey) {
        this.httpRequestFactory = httpRequestFactory;
        this.sdkKey = sdkKey;
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
}
