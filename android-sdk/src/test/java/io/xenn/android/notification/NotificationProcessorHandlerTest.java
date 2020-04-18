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

import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.service.EntitySerializerService;
import io.xenn.android.service.HttpService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
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
        when(entitySerializerService.serialize(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

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
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        Map<String, Object> externalParameters = new HashMap<>();
        externalParameters.put("pushId", "pushId");
        externalParameters.put("campaignId", "campaignId");
        externalParameters.put("campaignDate", "campaignDate");
        externalParameters.put("url", "url");
        externalParameters.put("utm_source", "xennio");
        externalParameters.put("utm_medium", "utm_medium");
        externalParameters.put("utm_campaign", "utm_campaign");
        externalParameters.put("utm_term", "utm_term");
        externalParameters.put("utm_content", "utm_content");

        when(sessionContextHolder.getExternalParameters()).thenReturn(externalParameters);
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serialize(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        notificationProcessorHandler.pushMessageReceived();

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals("Feedback", header.get("n"));
        assertEquals("sessionId", header.get("s"));
        assertEquals("persistentId", header.get("p"));
        assertEquals("memberId", body.get("memberId"));
        assertEquals("pushReceived", body.get("type"));
        assertEquals("utm_content", body.get("utm_content"));
        assertEquals("utm_term", body.get("utm_term"));
        assertEquals("utm_medium", body.get("utm_medium"));
        assertEquals("utm_campaign", body.get("utm_campaign"));
        assertEquals("xennio", body.get("utm_source"));
        assertEquals("url", body.get("url"));
        assertEquals("campaignDate", body.get("campaignDate"));
        assertEquals("campaignId", body.get("campaignId"));
        assertEquals("pushId", body.get("pushId"));

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

    @Test
    public void it_should_construct_push_message_open_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        Map<String, Object> externalParameters = new HashMap<>();
        externalParameters.put("pushId", "pushId");
        externalParameters.put("campaignId", "campaignId");
        externalParameters.put("campaignDate", "campaignDate");
        externalParameters.put("url", "url");
        externalParameters.put("utm_source", "xennio");
        externalParameters.put("utm_medium", "utm_medium");
        externalParameters.put("utm_campaign", "utm_campaign");
        externalParameters.put("utm_term", "utm_term");
        externalParameters.put("utm_content", "utm_content");

        when(sessionContextHolder.getExternalParameters()).thenReturn(externalParameters);
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serialize(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        notificationProcessorHandler.pushMessageOpened();

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals("Feedback", header.get("n"));
        assertEquals("sessionId", header.get("s"));
        assertEquals("persistentId", header.get("p"));
        assertEquals("memberId", body.get("memberId"));
        assertEquals("pushOpened", body.get("type"));
        assertEquals("utm_content", body.get("utm_content"));
        assertEquals("utm_term", body.get("utm_term"));
        assertEquals("utm_medium", body.get("utm_medium"));
        assertEquals("utm_campaign", body.get("utm_campaign"));
        assertEquals("xennio", body.get("utm_source"));
        assertEquals("url", body.get("url"));
        assertEquals("campaignDate", body.get("campaignDate"));
        assertEquals("campaignId", body.get("campaignId"));
        assertEquals("pushId", body.get("pushId"));

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

}