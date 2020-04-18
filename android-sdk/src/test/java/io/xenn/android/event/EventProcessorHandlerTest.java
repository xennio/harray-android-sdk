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

import static org.junit.Assert.*;
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
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serialize(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        eventProcessorHandler.pageView("homePage");

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals(header.get("n"), "PV");
        assertEquals(header.get("s"), "sessionId");
        assertEquals(header.get("p"), "persistentId");
        assertEquals(body.get("memberId"), "memberId");
        assertEquals(body.get("pageType"), "homePage");

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }


    @Test
    public void it_should_construct_page_view_and_append_extra_variables_to_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serialize(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");
        HashMap<String, Object> extraParams = new HashMap<>();
        extraParams.put("e1", "2");
        extraParams.put("e2", 4);
        eventProcessorHandler.pageView("homePage", extraParams);

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals(header.get("n"), "PV");
        assertEquals(header.get("s"), "sessionId");
        assertEquals(header.get("p"), "persistentId");
        assertEquals(body.get("memberId"), "memberId");
        assertEquals(body.get("pageType"), "homePage");
        assertEquals(body.get("e1"), "2");
        assertEquals(body.get("e2"), 4);

        verify(httpService).postFormUrlEncoded("serializedEntity");


    }

    @Test
    public void it_should_construct_action_result_and_append_extra_variables_to_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serialize(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");
        HashMap<String, Object> extraParams = new HashMap<>();
        extraParams.put("e1", "2");
        extraParams.put("e2", 4);
        eventProcessorHandler.actionResult("conversion", extraParams);

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals(header.get("n"), "AR");
        assertEquals(header.get("s"), "sessionId");
        assertEquals(header.get("p"), "persistentId");
        assertEquals(body.get("memberId"), "memberId");
        assertEquals(body.get("type"), "conversion");
        assertEquals(body.get("e1"), "2");
        assertEquals(body.get("e2"), 4);

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

    @Test
    public void it_should_construct_action_result_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn(null);
        when(entitySerializerService.serialize(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");
        eventProcessorHandler.actionResult("conversion");

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals(header.get("n"), "AR");
        assertEquals(header.get("s"), "sessionId");
        assertEquals(header.get("p"), "persistentId");
        assertEquals(body.get("memberId"), null);
        assertEquals(body.get("type"), "conversion");

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

    @Test
    public void it_should_construct_impression_and_append_extra_variables_to_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        when(entitySerializerService.serialize(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");
        HashMap<String, Object> extraParams = new HashMap<>();
        extraParams.put("e1", "2");
        extraParams.put("e2", 4);
        eventProcessorHandler.impression("product", extraParams);

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals(header.get("n"), "IM");
        assertEquals(header.get("s"), "sessionId");
        assertEquals(header.get("p"), "persistentId");
        assertEquals(body.get("memberId"), "memberId");
        assertEquals(body.get("type"), "product");
        assertEquals(body.get("e1"), "2");
        assertEquals(body.get("e2"), 4);

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }

    @Test
    public void it_should_construct_impression_event_and_make_api_call() throws UnsupportedEncodingException {

        ArgumentCaptor<Map<String, Object>> xennEventArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(applicationContextHolder.getPersistentId()).thenReturn("persistentId");
        when(sessionContextHolder.getSessionIdAndExtendSession()).thenReturn("sessionId");
        when(sessionContextHolder.getMemberId()).thenReturn(null);
        when(entitySerializerService.serialize(xennEventArgumentCaptor.capture())).thenReturn("serializedEntity");

        eventProcessorHandler.impression("product");

        Map<String, Object> xennEventMap = xennEventArgumentCaptor.getValue();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals(header.get("n"), "IM");
        assertEquals(header.get("s"), "sessionId");
        assertEquals(header.get("p"), "persistentId");
        assertEquals(body.get("memberId"), null);
        assertEquals(body.get("type"), "product");

        verify(httpService).postFormUrlEncoded("serializedEntity");
    }


}