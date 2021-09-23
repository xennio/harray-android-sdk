package io.xenn.android.event.inappnotification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.InjectMocks;

import java.util.List;
import java.util.Map;

import io.xenn.android.common.ResponseBodyHandler;
import io.xenn.android.common.ResultConsumer;
import io.xenn.android.common.XennConfig;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.event.EventProcessorHandler;
import io.xenn.android.event.RecommendationProcessorHandler;
import io.xenn.android.model.inappnotification.InAppNotificationResponse;
import io.xenn.android.service.HttpService;
import io.xenn.android.service.JsonDeserializerService;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InAppNotificationProcessorHandlerTest {

    @Mock
    private EventProcessorHandler eventProcessorHandler;

    @Mock
    private ApplicationContextHolder applicationContextHolder;

    @Mock
    private SessionContextHolder sessionContextHolder;

    @Mock
    private HttpService httpService;

    @Mock
    private JsonDeserializerService jsonDeserializerService;

    @Captor
    private ArgumentCaptor<Map<String, Object>> paramCaptor;

    @Mock
    private LinkClickHandler linkClickHandler;


    @Test
    public void it_should_get_in_app_notifications() {
        XennConfig xennConfig = XennConfig.init("sdk-key").inAppNotificationLinkClickHandler(linkClickHandler);

        InAppNotificationProcessorHandler inAppNotificationProcessorHandler = new InAppNotificationProcessorHandler(
                eventProcessorHandler, applicationContextHolder, sessionContextHolder, httpService, jsonDeserializerService, xennConfig
        );

        when(applicationContextHolder.getPersistentId()).thenReturn("pid");
        when(sessionContextHolder.getMemberId()).thenReturn("memberId");
        inAppNotificationProcessorHandler.callAfter(null);

        verify(httpService).getApiRequest(eq("/in-app-notifications"), paramCaptor.capture(), any(ResponseBodyHandler.class), any(ResultConsumer.class));
        Map<String, Object> capturedParams = paramCaptor.getValue();
        assertEquals(capturedParams.get("sdkKey"), "sdk-key");
        assertEquals(capturedParams.get("source"), "android");
        assertEquals(capturedParams.get("pid"), "pid");
        assertEquals(capturedParams.get("memberId"), "memberId");
    }

    @Test
    public void it_should_get_in_app_notifications_without_memberId_if_not_exists() {

        XennConfig xennConfig = XennConfig.init("sdk-key").inAppNotificationLinkClickHandler(linkClickHandler);

        InAppNotificationProcessorHandler inAppNotificationProcessorHandler = new InAppNotificationProcessorHandler(
                eventProcessorHandler, applicationContextHolder, sessionContextHolder, httpService,  jsonDeserializerService, xennConfig
        );

        when(applicationContextHolder.getPersistentId()).thenReturn("pid");
        when(sessionContextHolder.getMemberId()).thenReturn(null);
        inAppNotificationProcessorHandler.callAfter(null);

        verify(httpService).getApiRequest(eq("/in-app-notifications"), paramCaptor.capture(), any(ResponseBodyHandler.class), any(ResultConsumer.class));
        Map<String, Object> capturedParams = paramCaptor.getValue();
        assertEquals(capturedParams.get("sdkKey"), "sdk-key");
        assertEquals(capturedParams.get("source"), "android");
        assertEquals(capturedParams.get("pid"), "pid");
        assertEquals(capturedParams.get("memberId"), null);
    }

    @Test
    public void it_should_add_page_type_when_page_type_exists() {

        XennConfig xennConfig = XennConfig.init("sdk-key").inAppNotificationLinkClickHandler(linkClickHandler);

        InAppNotificationProcessorHandler inAppNotificationProcessorHandler = new InAppNotificationProcessorHandler(
                eventProcessorHandler, applicationContextHolder, sessionContextHolder, httpService,  jsonDeserializerService, xennConfig
        );

        when(applicationContextHolder.getPersistentId()).thenReturn("pid");
        when(sessionContextHolder.getMemberId()).thenReturn(null);
        inAppNotificationProcessorHandler.callAfter("homePage");

        verify(httpService).getApiRequest(eq("/in-app-notifications"), paramCaptor.capture(), any(ResponseBodyHandler.class), any(ResultConsumer.class));
        Map<String, Object> capturedParams = paramCaptor.getValue();
        assertEquals(capturedParams.get("sdkKey"), "sdk-key");
        assertEquals(capturedParams.get("source"), "android");
        assertEquals(capturedParams.get("pid"), "pid");
        assertEquals(capturedParams.get("pageType"), "homePage");
        assertEquals(capturedParams.get("memberId"), null);
    }
}