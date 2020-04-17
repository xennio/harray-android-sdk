package io.xenn.android.http;

public class HttpRequestFactory {
    public PostFormUrlEncodedTask getPostFormUrlEncodedTask(String endpoint, String payload) {
        return new PostFormUrlEncodedTask(endpoint, payload);
    }
}
