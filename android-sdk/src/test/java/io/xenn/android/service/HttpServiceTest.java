package io.xenn.android.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.xenn.android.http.PostFormUrlEncodedTask;
import io.xenn.android.service.http.HttpRequestFactory;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpServiceTest {

    @Mock
    private HttpRequestFactory httpRequestFactory;

    @Mock
    private PostFormUrlEncodedTask mockPostFormUrlEncodedTask;

    @Test
    public void it_should_make_form_url_encoded_request() {
        String endpoint = "endpoint";
        HttpService httpService = new HttpService(endpoint, httpRequestFactory);
        String payload = "payload";
        when(httpRequestFactory.getPostFormUrlEncodedTask(endpoint, payload)).thenReturn(mockPostFormUrlEncodedTask);
        httpService.postFormUrlEncoded(payload);
        verify(mockPostFormUrlEncodedTask).execute();
    }
}