package io.xenn.android.event;

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
import io.xenn.android.service.DeviceService;
import io.xenn.android.service.EntitySerializerService;
import io.xenn.android.service.HttpService;
import io.xenn.android.utils.ClockUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
        HashMap<String, Object> externalParameters = new HashMap<>();
        externalParameters.put("utm_source", "xennio");
        when(sessionContextHolder.getExternalParameters()).thenReturn(externalParameters);
        when(sessionContextHolder.getMemberId()).thenReturn(null);
        when(entitySerializerService.serializeToBase64(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");


        when(deviceService.getManufacturer()).thenReturn("Samsung");
        when(deviceService.getOsVersion()).thenReturn("Kitkat");
        when(deviceService.getBrand()).thenReturn("Galaxy 12");
        when(deviceService.getCarrier()).thenReturn("ATT");
        when(deviceService.getAppVersion()).thenReturn("1.2");

        sdkEventProcessorHandler.sessionStart();

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals("SS", header.get("n"));
        assertEquals("sessionId", header.get("s"));
        assertEquals("persistentId", header.get("p"));
        assertEquals("Samsung", body.get("mn"));
        assertEquals("Galaxy 12", body.get("br"));
        assertEquals("ATT", body.get("op"));
        assertEquals("1.2", body.get("av"));
        assertEquals("Android Kitkat", body.get("os"));
        assertEquals("3", body.get("zn"));
        assertEquals("xennio", body.get("utm_source"));
        assertNull(body.get("memberId"));


        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

    @Test
    public void it_should_construct_heart_beat_event_and_make_api_call() throws UnsupportedEncodingException {

        ClockUtils.freeze(1587237170000L);
        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(sessionContextHolder.getLastActivityTime()).thenReturn(1584558770000L);
        when(entitySerializerService.serializeToBase64(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        sdkEventProcessorHandler.heartBeat();

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals("HB", header.get("n"));
        assertEquals("sessionId", header.get("s"));
        assertEquals("persistentId", header.get("p"));
        assertEquals("memberId", body.get("memberId"));

        verify(httpService).postFormUrlEncoded("serializedEntity");
        ClockUtils.unFreeze();
    }

    @Test
    public void it_should_not_send_heart_beat_event_when_last_event_time_is_not_before_current_time_minus_interval() throws UnsupportedEncodingException {
        ClockUtils.freeze(1587237294000L);
        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(sessionContextHolder.getLastActivityTime()).thenReturn(1587237260000L);

        sdkEventProcessorHandler.heartBeat();

        verifyNoInteractions(httpService);
        ClockUtils.unFreeze();
    }
}