package io.xenn.android.event;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.xenn.android.common.Constants;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.model.XennEvent;
import io.xenn.android.service.DeviceService;
import io.xenn.android.service.EntitySerializerService;
import io.xenn.android.service.HttpService;
import io.xenn.android.utils.ClockUtils;
import io.xenn.android.utils.XennioLogger;

public class SDKEventProcessorHandler {
    private final ApplicationContextHolder applicationContextHolder;
    private final SessionContextHolder sessionContextHolder;
    private final HttpService httpService;
    private final EntitySerializerService entitySerializerService;
    private final DeviceService deviceService;
    private static final Long HEART_BEAT_INTERVAL = 55 * 1000L;

    private Timer timer = new Timer();
    private TimerTask heartBeatTask;


    public SDKEventProcessorHandler(ApplicationContextHolder applicationContextHolder, SessionContextHolder sessionContextHolder, HttpService httpService, EntitySerializerService entitySerializerService, DeviceService deviceService) {
        this.applicationContextHolder = applicationContextHolder;
        this.sessionContextHolder = sessionContextHolder;
        this.httpService = httpService;
        this.entitySerializerService = entitySerializerService;
        this.deviceService = deviceService;
        this.registerProcessLifecycle();
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
                    .appendExtra(sessionContextHolder.getExternalParameters())
                    .toMap();
            String serializedEntity = entitySerializerService.serializeToBase64(event);
            httpService.postFormUrlEncoded(serializedEntity);

        } catch (Exception e) {
            XennioLogger.log("Session start error: " + e.getMessage());
        }
    }

    public void heartBeat() {
        if (sessionContextHolder.getLastActivityTime() < ClockUtils.getTime() - HEART_BEAT_INTERVAL) {
            try {
                Map<String, Object> event = XennEvent.create("HB", applicationContextHolder.getPersistentId(), sessionContextHolder.getSessionIdAndExtendSession())
                        .memberId(sessionContextHolder.getMemberId())
                        .toMap();
                String serializedEntity = entitySerializerService.serializeToBase64(event);
                httpService.postFormUrlEncoded(serializedEntity);

            } catch (Exception e) {
                XennioLogger.log("Heart beat error: " + e.getMessage());
            }
        }
    }

    private void registerProcessLifecycle() {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(
                new LifecycleObserver() {
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    void onMoveToForeground() {
                        scheduleTimer();
                    }

                    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
                    void onMoveToBackground() {
                        cancelTimer();
                    }
                });
    }

    private void cancelTimer() {
        timer.purge();
        timer.cancel();
        XennioLogger.log("Heart beat task cancelled");
    }

    private void scheduleTimer() {
        final SDKEventProcessorHandler processorHandler = this;
        timer.purge();
        timer.cancel();
        timer = new Timer();
        heartBeatTask = new TimerTask() {
            @Override
            public void run() {
                processorHandler.heartBeat();
            }
        };
        timer.schedule(heartBeatTask, HEART_BEAT_INTERVAL);
        XennioLogger.log("Heart beat task initialized");
    }
}
