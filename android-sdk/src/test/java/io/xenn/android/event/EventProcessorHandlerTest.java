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
import io.xenn.android.service.EntitySerializerService;
import io.xenn.android.service.HttpService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventProcessorHandlerTest {

    @InjectMocks
    private EventProcessorHandler eventProcessorHandler;

    @Mock
    private ApplicationContextHolder applicationContextHolder;

    @Mock
    private SessionContextHolder sessionContextHolder;

    @Mock
    private EntitySerializerService entitySerializerService;

    @Mock
    private HttpService httpService;

    @Test
    public void it_should_construct_page_view_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        HashMap<String, Object> externalParameters = new HashMap<>();
        externalParameters.put("utm_source", "xennio");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serializeToBase64(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        eventProcessorHandler.pageView("homePage");

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals("PV", header.get("n"));
        assertEquals("sessionId", header.get("s"));
        assertEquals("persistentId", header.get("p"));
        assertEquals("memberId", body.get("memberId"));
        assertEquals("homePage", body.get("pageType"));

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }


    @Test
    public void it_should_construct_page_view_and_append_extra_variables_to_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serializeToBase64(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");
        HashMap<String, Object> extraParams = new HashMap<>();
        extraParams.put("e1", "2");
        extraParams.put("e2", 4);
        eventProcessorHandler.pageView("homePage", extraParams);

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals("PV", header.get("n"));
        assertEquals("sessionId", header.get("s"));
        assertEquals("persistentId", header.get("p"));
        assertEquals("memberId", body.get("memberId"));
        assertEquals("homePage", body.get("pageType"));
        assertEquals("2", body.get("e1"));
        assertEquals(4, body.get("e2"));

        verify(httpService).postFormUrlEncoded("serializedEntity");


    }

    @Test
    public void it_should_construct_action_result_and_append_extra_variables_to_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serializeToBase64(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");
        HashMap<String, Object> extraParams = new HashMap<>();
        extraParams.put("e1", "2");
        extraParams.put("e2", 4);
        eventProcessorHandler.actionResult("conversion", extraParams);

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals("AR", header.get("n"));
        assertEquals("sessionId", header.get("s"));
        assertEquals("persistentId", header.get("p"));
        assertEquals("memberId", body.get("memberId"));
        assertEquals("conversion", body.get("type"));
        assertEquals("2", body.get("e1"));
        assertEquals(4, body.get("e2"));

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

    @Test
    public void it_should_construct_action_result_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn(null);
        when(entitySerializerService.serializeToBase64(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");
        eventProcessorHandler.actionResult("conversion");

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals("AR", header.get("n"));
        assertEquals("sessionId", header.get("s"));
        assertEquals("persistentId", header.get("p"));
        assertEquals("conversion", body.get("type"));
        assertNull(body.get("memberId"));
        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

    @Test
    public void it_should_construct_impression_and_append_extra_variables_to_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serializeToBase64(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");
        HashMap<String, Object> extraParams = new HashMap<>();
        extraParams.put("e1", "2");
        extraParams.put("e2", 4);
        eventProcessorHandler.impression("product", extraParams);

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals("IM", header.get("n"));
        assertEquals("sessionId", header.get("s"));
        assertEquals("persistentId", header.get("p"));
        assertEquals("memberId", body.get("memberId"));
        assertEquals("product", body.get("type"));
        assertEquals("2", body.get("e1"));
        assertEquals(4, body.get("e2"));

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

    @Test
    public void it_should_construct_impression_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn(null);
        when(entitySerializerService.serializeToBase64(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        HashMap<String, Object> extraParams = new HashMap<>();
        extraParams.put("e1", "2");
        extraParams.put("e2", 4);

        eventProcessorHandler.impression("product", extraParams);

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals("IM", header.get("n"));
        assertEquals("sessionId", header.get("s"));
        assertEquals("persistentId", header.get("p"));
        assertEquals("product", body.get("type"));
        assertNull(body.get("memberId"));

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

    @Test
    public void it_should_create_custom_event_and_make_api_call() throws UnsupportedEncodingException {
        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serializeToBase64(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        HashMap<String, Object> extraParams = new HashMap<>();
        extraParams.put("e1", "2");
        extraParams.put("e2", 4);

        eventProcessorHandler.custom("eventName", extraParams);

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals("eventName", header.get("n"));
        assertEquals("sessionId", header.get("s"));
        assertEquals("persistentId", header.get("p"));
        assertEquals("2", body.get("e1"));
        assertEquals("memberId", body.get("memberId"));
        assertEquals(4, body.get("e2"));

        verify(httpService).postFormUrlEncoded("serializedEntity");


    }


}