package io.xenn.android.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import io.xenn.android.common.ResponseBodyHandler;
import io.xenn.android.common.ResultConsumer;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.service.HttpService;
import io.xenn.android.service.JsonDeserializerService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BrowsingHistoryProcessorHandlerTest {

    @Mock
    private ApplicationContextHolder applicationContextHolder;

    @Mock
    private SessionContextHolder sessionContextHolder;

    @Mock
    private HttpService httpService;

    @Mock
    private JsonDeserializerService jsonDeserializerService;

    @Captor
    private ArgumentCaptor<Map<String, String>> paramCaptor;

    @Test
    public void it_should_get_browsing_history() {
        BrowsingHistoryProcessorHandler browsingHistoryProcessorHandler = new BrowsingHistoryProcessorHandler(
                applicationContextHolder, sessionContextHolder, httpService, "sdk-key", jsonDeserializerService
        );

        ResultConsumer<List<Map<String, String>>> callback = new ResultConsumer<List<Map<String, String>>>() {
            @Override
            public void consume(List<Map<String, String>> data) {
            }
        };

        when(applicationContextHolder.getPersistentId()).thenReturn("pid");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");

        browsingHistoryProcessorHandler.getBrowsingHistory("entityName", 5, callback);

        verify(httpService).getApiRequest(eq("/browsing-history"), paramCaptor.capture(), any(ResponseBodyHandler.class), eq(callback));
        Map<String, String> capturedParams = paramCaptor.getValue();
        assertEquals(capturedParams.get("sdkKey"), "sdk-key");
        assertEquals(capturedParams.get("entityName"), "entityName");
        assertEquals(capturedParams.get("pid"), "pid");
        assertEquals(capturedParams.get("memberId"), "memberId");
        assertEquals(capturedParams.get("size"), "5");
    }

    @Test
    public void it_should_get_recommendation_with_null_memberId() {
        BrowsingHistoryProcessorHandler browsingHistoryProcessorHandler = new BrowsingHistoryProcessorHandler(
                applicationContextHolder, sessionContextHolder, httpService, "sdk-key", jsonDeserializerService
        );

        ResultConsumer<List<Map<String, String>>> callback = new ResultConsumer<List<Map<String, String>>>() {
            @Override
            public void consume(List<Map<String, String>> data) {
            }
        };

        when(applicationContextHolder.getPersistentId()).thenReturn("pid");
        when(sessionContextHolder.getMemberId()).thenReturn(null);

        browsingHistoryProcessorHandler.getBrowsingHistory("entityName", 5, callback);

        verify(httpService).getApiRequest(eq("/browsing-history"), paramCaptor.capture(), any(ResponseBodyHandler.class), eq(callback));
        Map<String, String> capturedParams = paramCaptor.getValue();
        assertEquals(capturedParams.get("sdkKey"), "sdk-key");
        assertEquals(capturedParams.get("entityName"), "entityName");
        assertEquals(capturedParams.get("pid"), "pid");
        assertNull(capturedParams.get("memberId"));
        assertEquals(capturedParams.get("size"), "5");
    }
}