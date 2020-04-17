package io.xenn.android.service.http;

import org.junit.Test;

import io.xenn.android.http.PostFormUrlEncodedTask;

import static org.junit.Assert.*;

public class HttpRequestFactoryTest {

    @Test
    public void it_should_return_post_form_url_encoded_task() {
        HttpRequestFactory httpRequestFactory = new HttpRequestFactory();
        String endpoint = "a";
        String payload = "b";
        PostFormUrlEncodedTask postFormUrlEncodedTask = httpRequestFactory.getPostFormUrlEncodedTask(endpoint, payload);
        assertEquals(postFormUrlEncodedTask.getEndpoint(), endpoint);
        assertEquals(postFormUrlEncodedTask.getPayload(), payload);
    }

}