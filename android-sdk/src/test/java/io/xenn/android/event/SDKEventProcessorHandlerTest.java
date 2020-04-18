package io.xenn.android.event;

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
import io.xenn.android.service.DeviceService;
import io.xenn.android.service.EntitySerializerService;
import io.xenn.android.service.HttpService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SDKEventProcessorHandlerTest {

    @InjectMocks
    private SDKEventProcessorHandler sdkEventProcessorHandler;

    @Mock
    private ApplicationContextHolder applicationContextHolder;
    @Mock
    private SessionContextHolder sessionContextHolder;
    @Mock
    private HttpService httpService;
    @Mock
    private EntitySerializerService entitySerializerService;
    @Mock
    private DeviceService deviceService;

    @Test
    public void it_should_construct_session_start_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(applicationContextHolder.getTimezone()).thenReturn("3");

        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn(null);
        when(entitySerializerService.serialize(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        when(deviceService.getManufacturer()).thenReturn("Samsung");
        when(deviceService.getOsVersion()).thenReturn("Kitkat");
        when(deviceService.getBrand()).thenReturn("Galaxy 12");
        when(deviceService.getCarrier()).thenReturn("ATT");
        when(deviceService.getAppVersion()).thenReturn("1.2");

        sdkEventProcessorHandler.sessionStart();

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals(header.get("n"), "SS");
        assertEquals(header.get("s"), "sessionId");
        assertEquals(header.get("p"), "persistentId");
        assertEquals(body.get("memberId"), null);
        assertEquals(body.get("mn"), "Samsung");
        assertEquals(body.get("br"), "Galaxy 12");
        assertEquals(body.get("op"), "ATT");
        assertEquals(body.get("av"), "1.2");
        assertEquals(body.get("os"), "Android Kitkat");
        assertEquals(body.get("zn"), "3");

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

    @Test
    public void it_should_construct_heart_beat_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serialize(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        sdkEventProcessorHandler.heartBeat();

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals(header.get("n"), "HB");
        assertEquals(header.get("s"), "sessionId");
        assertEquals(header.get("p"), "persistentId");
        assertEquals(body.get("memberId"), "memberId");

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }


}