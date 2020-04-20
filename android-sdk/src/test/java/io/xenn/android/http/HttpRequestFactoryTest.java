package io.xenn.android.http;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpRequestFactoryTest {

    @Test
    public void it_should_return_post_form_url_encoded_task() {
        HttpRequestFactory httpRequestFactory = new HttpRequestFactory();
        String endpoint = "a";
        String payload = "b";
        PostFormUrlEncodedTask postFormUrlEncodedTask = httpRequestFactory.getPostFormUrlEncodedTask(endpoint, payload);
        assertEquals(endpoint, postFormUrlEncodedTask.getEndpoint());
        assertEquals(payload, postFormUrlEncodedTask.getPayload());
    }

    @Test
    public void it_should_return_bitmap_download_task() {
        HttpRequestFactory httpRequestFactory = new HttpRequestFactory();
        String endpoint = "a";
        BitmapDownloadTask bitmapDownloadTask = httpRequestFactory.getBitmapDownloadTask(endpoint);
        assertEquals(endpoint, bitmapDownloadTask.getUrlString());
    }

}