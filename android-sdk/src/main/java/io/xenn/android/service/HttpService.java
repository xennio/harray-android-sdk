package io.xenn.android.service;

import io.xenn.android.http.PostFormUrlEncodedTask;
import io.xenn.android.http.HttpRequestFactory;

public class HttpService {

    private final String endpoint;
    private final HttpRequestFactory httpRequestFactory;

    public HttpService(String endpoint, HttpRequestFactory httpRequestFactory) {
        this.endpoint = endpoint;
        this.httpRequestFactory = httpRequestFactory;
    }

    public void postFormUrlEncoded(final String payload) {
        PostFormUrlEncodedTask task = httpRequestFactory.getPostFormUrlEncodedTask(endpoint, payload);
        task.execute();
    }
}
