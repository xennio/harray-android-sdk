package io.xenn.android.http;

public class HttpRequestFactory {
    public PostFormUrlEncodedTask getPostFormUrlEncodedTask(String endpoint, String payload) {
        return new PostFormUrlEncodedTask(endpoint, payload);
    }

    public BitmapDownloadTask getBitmapDownloadTask(String endpoint) {
        return new BitmapDownloadTask(endpoint);
    }

    public PostJsonEncodedTask getPostJsonEncodedTask(String endpoint, String payload) {
        return new PostJsonEncodedTask(endpoint, payload);
    }
}
