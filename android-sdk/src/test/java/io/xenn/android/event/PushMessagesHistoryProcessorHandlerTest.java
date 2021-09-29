package io.xenn.android.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
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
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PushMessagesHistoryProcessorHandlerTest {

    @Mock
    private SessionContextHolder sessionContextHolder;

    @Mock
    private HttpService httpService;

    @Mock
    private JsonDeserializerService jsonDeserializerService;

    @Captor
    private ArgumentCaptor<Map<String, Object>> paramCaptor;

    @Test
    public void it_should_get_push_messages_history() {
        PushMessagesHistoryProcessorHandler handler = new PushMessagesHistoryProcessorHandler(
                sessionContextHolder, httpService, "sdk-key", jsonDeserializerService
        );

        ResultConsumer<List<Map<String, String>>> callback = new ResultConsumer<List<Map<String, String>>>() {
            @Override
            public void consume(List<Map<String, String>> data) {
            }
        };

        when(sessionContextHolder.getMemberId()).thenReturn("memberId");

        handler.getPushMessagesHistory(5, callback);

        verify(httpService).getApiRequest(eq("/push-messages-history"), paramCaptor.capture(), any(ResponseBodyHandler.class), eq(callback));
        Map<String, Object> capturedParams = paramCaptor.getValue();
        assertEquals(capturedParams.get("sdkKey"), "sdk-key");
        assertEquals(capturedParams.get("memberId"), "memberId");
        assertEquals(capturedParams.get("size"), "5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void it_should_not_get_push_messages_history_with_null_memberId() {
        PushMessagesHistoryProcessorHandler processorHandler = new PushMessagesHistoryProcessorHandler(
                sessionContextHolder, httpService, "sdk-key", jsonDeserializerService
        );

        ResultConsumer<List<Map<String, String>>> callback = new ResultConsumer<List<Map<String, String>>>() {
            @Override
            public void consume(List<Map<String, String>> data) {
            }
        };

        when(sessionContextHolder.getMemberId()).thenReturn(null);

        try {
            processorHandler.getPushMessagesHistory(5, callback);
            fail();
        } catch (IllegalArgumentException e) {
            verify(httpService, never()).getApiRequest(anyString(), ArgumentMatchers.<String, Object>anyMap(), any(ResponseBodyHandler.class), any(ResultConsumer.class));
            throw e;
        } catch (Exception e) {
            fail();
        }
    }
}