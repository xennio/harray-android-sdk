package io.xenn.android.event;


import java.util.Map;

import io.xenn.android.common.Constants;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.model.XennEvent;
import io.xenn.android.service.DeviceService;
import io.xenn.android.service.EntitySerializerService;
import io.xenn.android.service.HttpService;
import io.xenn.android.utils.XennioLogger;

public class SDKEventProcessorHandler {
    private final ApplicationContextHolder applicationContextHolder;
    private final SessionContextHolder sessionContextHolder;
    private final HttpService httpService;
    private final EntitySerializerService entitySerializerService;
    private final DeviceService deviceService;



    public SDKEventProcessorHandler(ApplicationContextHolder applicationContextHolder, SessionContextHolder sessionContextHolder, HttpService httpService, EntitySerializerService entitySerializerService, DeviceService deviceService) {
        this.applicationContextHolder = applicationContextHolder;
        this.sessionContextHolder = sessionContextHolder;
        this.httpService = httpService;
        this.entitySerializerService = entitySerializerService;
        this.deviceService = deviceService;
    }

    public void sessionStart() {
        try {
            Map<String, Object> event = XennEvent.create("SS", applicationContextHolder.getPersistentId(), sessionContextHolder.getSessionIdAndExtendSession())
                    .addHeader("sv", applicationContextHolder.getSdkVersion())
                    .memberId(sessionContextHolder.getMemberId())
                    .addBody("os", Constants.ANDROID)
                    .addBody("osv", deviceService.getOsVersion())
                    .addBody("mn", deviceService.getManufacturer())
                    .addBody("br", deviceService.getBrand())
                    .addBody("md", deviceService.getModel())
                    .addBody("op", deviceService.getCarrier())
                    .addBody("av", deviceService.getAppVersion())
                    .addBody("zn", applicationContextHolder.getTimezone())
                    .addBody("sw", deviceService.getScreenWidth())
                    .addBody("sh", deviceService.getScreenHeight())
                    .addBody("ln", deviceService.getLang())
                    .appendExtra(sessionContextHolder.getExternalParameters())
                    .toMap();
            String serializedEntity = entitySerializerService.serializeToBase64(event);
            httpService.postFormUrlEncoded(serializedEntity);

        } catch (Exception e) {
            XennioLogger.log("Session start error: " + e.getMessage());
        }
    }

    public void newInstallation() {
        try {
            Map<String, Object> event = XennEvent.create("NI", applicationContextHolder.getPersistentId(), sessionContextHolder.getSessionIdAndExtendSession())
                    .memberId(sessionContextHolder.getMemberId())
                    .toMap();
            String serializedEntity = entitySerializerService.serializeToBase64(event);
            httpService.postFormUrlEncoded(serializedEntity);

        } catch (Exception e) {
            XennioLogger.log("New Installation error: " + e.getMessage());
        }
    }
}
