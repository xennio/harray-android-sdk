package io.xenn.android.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import io.xenn.android.common.ResponseBodyHandler;
import io.xenn.android.common.ResultConsumer;
import io.xenn.android.http.BitmapDownloadTask;
import io.xenn.android.http.HttpGetTask;
import io.xenn.android.http.HttpRequestFactory;
import io.xenn.android.http.PostFormUrlEncodedTask;
import io.xenn.android.http.PostJsonEncodedTask;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpServiceTest {

    @Mock
    private HttpRequestFactory httpRequestFactory;

    @Mock
    private PostFormUrlEncodedTask mockPostFormUrlEncodedTask;

    @Mock
    private HttpGetTask<String> mockRecommendationHttpGetTask;

    @Mock
    private PostJsonEncodedTask mockPostJsonEncodedTask;

    @Mock
    private BitmapDownloadTask bitmapDownloadTask;

    @Test
    public void it_should_make_form_url_encoded_request() {
        HttpService httpService = new HttpService(httpRequestFactory, "sdk-key", "https://c.xenn.io:443", "https://api.xenn.io:443");
        String payload = "payload";
        when(httpRequestFactory.getPostFormUrlEncodedTask(httpService.getCollectorUrl(), "e=" + payload)).thenReturn(mockPostFormUrlEncodedTask);
        httpService.postFormUrlEncoded(payload);
        verify(mockPostFormUrlEncodedTask).execute();
    }

    @Test
    public void it_should_bitmap_request() {
        String endpoint = "endpoint";
        HttpService httpService = new HttpService(httpRequestFactory, "sdk-key", "https://c.xenn.io:443", "https://api.xenn.io:443");
        when(httpRequestFactory.getBitmapDownloadTask(endpoint)).thenReturn(bitmapDownloadTask);
        httpService.downloadImage(endpoint);
        verify(bitmapDownloadTask).getBitmap();
    }

    @Test
    public void it_should_make_json_encoded_request() {
        HttpService httpService = new HttpService(httpRequestFactory, "sdk-key", "https://c.xenn.io:443", "https://api.xenn.io:443");
        String payload = "{\"foo\":\"bar\"}";
        when(httpRequestFactory.getPostJsonEncodedTask(anyString(), eq(payload))).thenReturn(mockPostJsonEncodedTask);
        httpService.postJsonEncoded(payload, "feedback");
        verify(mockPostJsonEncodedTask).execute();
    }

    @Test
    public void it_should_add_sdk_key_to_collector_url() {
        String endpoint = "endpoint";
        HttpService httpService = new HttpService(httpRequestFactory, "sdk-key", "https://c.xenn.io:443", "https://api.xenn.io:443");
        assertEquals("https://c.xenn.io:443/sdk-key", httpService.getCollectorUrl());
    }

    @Test
    public void it_should_add_path_to_collector_url() {
        String endpoint = "endpoint";
        HttpService httpService = new HttpService(httpRequestFactory, "sdk-key", "https://c.xenn.io:443", "https://api.xenn.io:443");
        assertEquals("https://c.xenn.io:443/feedback", httpService.getCollectorUrl("feedback"));
    }

    @Test
    public void it_should_make_get_request() throws InterruptedException, ExecutionException, TimeoutException {
        ResponseBodyHandler<String> rh = new ResponseBodyHandler<String>() {
            @Override
            public String handle(String rawResponseBody) {
                return rawResponseBody;
            }
        };
        ResultConsumer<String> callback = new ResultConsumer<String>() {
            @Override
            public void consume(String data) {
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("param1", "val1");
        params.put("param2", "val2");
        HttpService httpService = new HttpService(httpRequestFactory, "sdk-key", "https://c.xenn.io:443", "https://api.xenn.io:443");
        when(httpRequestFactory.getHttpGetTask(
                "https://api.xenn.io:443/reco?param1=val1&param2=val2&",
                rh, callback
        )).thenReturn(mockRecommendationHttpGetTask);

        httpService.getApiRequest("/reco", params, rh, callback);

        verify(mockRecommendationHttpGetTask).execute();
    }
}