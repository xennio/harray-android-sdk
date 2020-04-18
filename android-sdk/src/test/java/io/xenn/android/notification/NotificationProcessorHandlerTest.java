package io.xenn.android.notification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.UnsupportedEncodingException;
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

        assertEquals(header.get("n"), "Collection");
        assertEquals(header.get("s"), "sessionId");
        assertEquals(header.get("p"), "persistentId");
        assertEquals(body.get("memberId"), "memberId");
        assertEquals(body.get("name"), "pushToken");
        assertEquals(body.get("type"), "fcmToken");
        assertEquals(body.get("appType"), "fcmAppPush");
        assertEquals(body.get("deviceToken"), "device token");

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

}