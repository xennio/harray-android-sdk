package io.xenn.android.notification;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.xenn.android.common.PushMessageDataWrapper;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.service.EntitySerializerService;
import io.xenn.android.service.HttpService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotificationProcessorHandlerTest {

    @InjectMocks
    private NotificationProcessorHandler notificationProcessorHandler;

    @Mock
    private ApplicationContextHolder applicationContextHolder;

    @Mock
    private SessionContextHolder sessionContextHolder;

    @Mock
    private EntitySerializerService entitySerializerService;

    @Mock
    private HttpService httpService;


    @Test
    public void it_should_construct_save_device_token_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serializeToBase64(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        notificationProcessorHandler.savePushToken("device token");

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals("Collection", header.get("n"));
        assertEquals("sessionId", header.get("s"));
        assertEquals("persistentId", header.get("p"));
        assertEquals("memberId", body.get("memberId"));
        assertEquals("pushToken", body.get("name"));
        assertEquals("fcmToken", body.get("type"));
        assertEquals("fcmAppPush", body.get("appType"));
        assertEquals("device token", body.get("deviceToken"));

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

    @Test
    public void it_should_construct_push_message_receive_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        Map<String, String> externalParameters = new HashMap<>();
        externalParameters.put("pushId", "pushId");
        externalParameters.put("campaignId", "campaignId");
        externalParameters.put("campaignDate", "campaignDate");
        externalParameters.put("url", "url");
        externalParameters.put("utm_source", "xennio");
        externalParameters.put("utm_medium", "utm_medium");
        externalParameters.put("utm_campaign", "utm_campaign");
        externalParameters.put("utm_term", "utm_term");
        externalParameters.put("utm_content", "utm_content");

        when(entitySerializerService.serializeToJson(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        notificationProcessorHandler.pushMessageDelivered(PushMessageDataWrapper.from(externalParameters));

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();

        assertEquals("d", xennEventMap.get("n"));
        assertEquals("pushId", xennEventMap.get("pi"));
        assertEquals("campaignId", xennEventMap.get("ci"));
        assertEquals("campaignDate", xennEventMap.get("cd"));

        verify(httpService).postJsonEncoded("serializedEntity", "feedback");
    }

    @Test
    public void it_should_construct_push_message_open_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        Map<String, String> externalParameters = new HashMap<>();
        externalParameters.put("pushId", "pushId");
        externalParameters.put("campaignId", "campaignId");
        externalParameters.put("campaignDate", "campaignDate");
        externalParameters.put("url", "url");
        externalParameters.put("source", "xennio");
        externalParameters.put("utm_medium", "utm_medium");
        externalParameters.put("utm_campaign", "utm_campaign");
        externalParameters.put("utm_term", "utm_term");
        externalParameters.put("utm_content", "utm_content");

        when(entitySerializerService.serializeToJson(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        notificationProcessorHandler.pushMessageOpened(PushMessageDataWrapper.from(externalParameters));

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();

        assertEquals("o", xennEventMap.get("n"));
        assertEquals("pushId", xennEventMap.get("pi"));
        assertEquals("campaignId", xennEventMap.get("ci"));
        assertEquals("campaignDate", xennEventMap.get("cd"));

        verify(httpService).postJsonEncoded("serializedEntity", "feedback");
    }

    @Test
    public void it_should_not_make_push_open_when_source_is_not_xenn_io() throws UnsupportedEncodingException {

        Map<String, String> externalParameters = new HashMap<>();
        externalParameters.put("pushId", "pushId");
        externalParameters.put("campaignId", "campaignId");
        externalParameters.put("campaignDate", "campaignDate");
        externalParameters.put("url", "url");
        externalParameters.put("source", "mennio");
        externalParameters.put("utm_medium", "utm_medium");
        externalParameters.put("utm_campaign", "utm_campaign");
        externalParameters.put("utm_term", "utm_term");
        externalParameters.put("utm_content", "utm_content");

        notificationProcessorHandler.pushMessageOpened(PushMessageDataWrapper.from(externalParameters));

        verifyNoInteractions(httpService);
    }

}