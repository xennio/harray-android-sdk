package io.xenn.android.http;

import io.xenn.android.common.ResultConsumer;
import io.xenn.android.common.ResponseBodyHandler;

public class HttpRequestFactory {

    public <T> HttpGetTask<T> getHttpGetTask(String endpoint,
                                             ResponseBodyHandler<T> rh,
                                             ResultConsumer<T> callback) {
        return new HttpGetTask<>(endpoint, rh, callback);
    }

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
