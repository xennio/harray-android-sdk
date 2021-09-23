package io.xenn.android.event;

import java.util.HashMap;
import java.util.Map;

import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.model.XennEvent;
import io.xenn.android.service.EntitySerializerService;
import io.xenn.android.service.HttpService;
import io.xenn.android.utils.XennioLogger;

public class EventProcessorHandler {

    private final ApplicationContextHolder applicationContextHolder;
    private final SessionContextHolder sessionContextHolder;
    private final HttpService httpService;
    private final EntitySerializerService entitySerializerService;
    private final Map<String, Object> EMPTY_MAP = new HashMap<>();
    private final ChainProcessorHandler chainProcessorHandler;

    public EventProcessorHandler(ApplicationContextHolder applicationContextHolder,
                                 SessionContextHolder sessionContextHolder,
                                 HttpService httpService,
                                 EntitySerializerService entitySerializerService,
                                 ChainProcessorHandler chainProcessorHandler) {
        this.applicationContextHolder = applicationContextHolder;
        this.sessionContextHolder = sessionContextHolder;
        this.httpService = httpService;
        this.entitySerializerService = entitySerializerService;
        this.chainProcessorHandler = chainProcessorHandler;
    }

    public void pageView(String pageType) {
        pageView(pageType, EMPTY_MAP);
    }


    public void pageView(String pageType, Map<String, Object> params) {
        Map<String, Object> pageViewEvent = XennEvent.create("PV", applicationContextHolder.getPersistentId(), sessionContextHolder.getSessionIdAndExtendSession())
                .addBody("pageType", pageType)
                .memberId(sessionContextHolder.getMemberId())
                .appendExtra(params)
                .toMap();

        try {
            String serializedEvent = entitySerializerService.serializeToBase64(pageViewEvent);
            httpService.postFormUrlEncoded(serializedEvent);
            chainProcessorHandler.callAll(pageType);
        } catch (Exception e) {
            XennioLogger.log("Page View Event Error:" + e.getMessage());
        }
    }

    public void actionResult(String type) {
        actionResult(type, EMPTY_MAP);
    }

    public void actionResult(String type, Map<String, Object> params) {
        try {
            Map<String, Object> actionResultEvent = XennEvent
                    .create("AR", applicationContextHolder.getPersistentId(), sessionContextHolder.getSessionIdAndExtendSession())
                    .memberId(sessionContextHolder.getMemberId())
                    .addBody("type", type)
                    .appendExtra(params)
                    .toMap();
            String serializedEvent = entitySerializerService.serializeToBase64(actionResultEvent);
            httpService.postFormUrlEncoded(serializedEvent);
        } catch (Exception e) {
            XennioLogger.log("Action Result Event Error:" + e.getMessage());
        }
    }

    public void impression(String type) {
        impression(type, EMPTY_MAP);
    }

    public void impression(String type, Map<String, Object> params) {
        try {
            Map<String, Object> impressionEvent = XennEvent.create("IM", applicationContextHolder.getPersistentId(), sessionContextHolder.getSessionIdAndExtendSession())
                    .memberId(sessionContextHolder.getMemberId())
                    .addBody("type", type)
                    .appendExtra(params)
                    .toMap();
            String serializedEvent = entitySerializerService.serializeToBase64(impressionEvent);
            httpService.postFormUrlEncoded(serializedEvent);
        } catch (Exception e) {
            XennioLogger.log("Impression Event Error:" + e.getMessage());
        }

    }

    public void custom(String eventName, Map<String, Object> params) {
        try {
            Map<String, Object> impressionEvent = XennEvent.create(eventName, applicationContextHolder.getPersistentId(), sessionContextHolder.getSessionIdAndExtendSession())
                    .memberId(sessionContextHolder.getMemberId())
                    .appendExtra(params)
                    .toMap();
            String serializedEvent = entitySerializerService.serializeToBase64(impressionEvent);
            httpService.postFormUrlEncoded(serializedEvent);
        } catch (Exception e) {
            XennioLogger.log(eventName + "Event Error:" + e.getMessage());
        }
    }
}
