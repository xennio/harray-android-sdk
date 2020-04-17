package io.xenn.android.service.http;

import io.xenn.android.http.PostFormUrlEncodedTask;

public class HttpRequestFactory {
    public PostFormUrlEncodedTask getPostFormUrlEncodedTask(String endpoint, String payload) {
        return new PostFormUrlEncodedTask(endpoint, payload);
    }
}
