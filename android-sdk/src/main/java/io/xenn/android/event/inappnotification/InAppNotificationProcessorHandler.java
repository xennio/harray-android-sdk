package io.xenn.android.event.inappnotification;

import android.app.Activity;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.xenn.android.common.ResponseBodyHandler;
import io.xenn.android.common.ResultConsumer;
import io.xenn.android.common.XennConfig;
import io.xenn.android.context.ActivityLifecycleListener;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.event.AfterPageViewEventHandler;
import io.xenn.android.event.EventProcessorHandler;
import io.xenn.android.model.inappnotification.InAppNotificationHandlerStrategy;
import io.xenn.android.model.inappnotification.InAppNotificationResponse;
import io.xenn.android.service.HttpService;
import io.xenn.android.service.JsonDeserializerService;
import io.xenn.android.utils.XennioLogger;

public class InAppNotificationProcessorHandler implements AfterPageViewEventHandler {

    private static final Long CHECK_NOTIFICATION_INTERVAL = 20 * 1000L;

    private Timer timer = new Timer();

    private final EventProcessorHandler eventProcessorHandler;
    private final HttpService httpService;
    private final ApplicationContextHolder applicationContextHolder;
    private final JsonDeserializerService jsonDeserializerService;
    private final SessionContextHolder sessionContextHolder;
    private final XennConfig xennConfig;
    private final Map<String,Object> requestParameters = new HashMap<>();

    public InAppNotificationProcessorHandler(
            EventProcessorHandler eventProcessorHandler,
            ApplicationContextHolder applicationContextHolder,
            SessionContextHolder sessionContextHolder,
            HttpService httpService,
            JsonDeserializerService jsonDeserializerService,
            XennConfig xennConfig
            ) {
        this.eventProcessorHandler = eventProcessorHandler;
        this.httpService = httpService;
        this.xennConfig = xennConfig;
        this.jsonDeserializerService = jsonDeserializerService;
        this.applicationContextHolder = applicationContextHolder;
        this.sessionContextHolder = sessionContextHolder;

        if(this.xennConfig.getInAppNotificationHandlerStrategy() == InAppNotificationHandlerStrategy.TimerBased) {
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

        requestParameters.put("source", "android");

    }

    private void scheduleTimer() {
        timer.purge();
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                callAfter(null);
            }
        }, 1L, CHECK_NOTIFICATION_INTERVAL);
        XennioLogger.log("Xenn in-app notification task initialized");
    }

    private void cancelTimer() {
        timer.purge();
        timer.cancel();
        XennioLogger.log("Xenn in-app notification task cancelled");
    }

    private void showInAppNotification(@Nullable InAppNotificationResponse inAppNotificationResponse) {
        if (inAppNotificationResponse == null) {
            XennioLogger.log("There is no in-app notification response to be processed");
            return;
        }
        Activity activity = ActivityLifecycleListener.getCurrentActivity();
        if (activity == null) {
            XennioLogger.log("There is activity to show in-app notification");
            return;
        }
        delayShowUntilAvailable(activity, inAppNotificationResponse);
    }

    private void delayShowUntilAvailable(final Activity activity, final InAppNotificationResponse inAppNotificationResponse) {
        if (isActivityReady(activity)) {
            new InAppNotificationViewManager(
                    activity, inAppNotificationResponse, xennConfig.getInAppNotificationLinkClickHandler(), createShowEventHandler(inAppNotificationResponse), createCloseEventHandler(inAppNotificationResponse)
            ).show();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    delayShowUntilAvailable(activity, inAppNotificationResponse);
                }
            }, 50);
        }
    }

    private boolean isActivityReady(@NonNull Activity activity) {
        return activity.getWindow().getDecorView().getApplicationWindowToken() != null;
    }

    private Runnable createShowEventHandler(final InAppNotificationResponse inAppNotificationResponse) {
        return new Runnable() {
            @Override
            public void run() {
                Map<String, Object> eventParams = new HashMap<>();
                eventParams.put("entity", "banners");
                eventParams.put("id", inAppNotificationResponse.getId());
                eventProcessorHandler.actionResult("bannerShow", eventParams);
            }
        };
    }

    private Runnable createCloseEventHandler(final InAppNotificationResponse inAppNotificationResponse) {
        return new Runnable() {
            @Override
            public void run() {
                Map<String, Object> eventParams = new HashMap<>();
                eventParams.put("entity", "banners");
                eventParams.put("id", inAppNotificationResponse.getId());
                eventParams.put("action", "close");
                eventProcessorHandler.actionResult("bannerClose", eventParams);
            }
        };
    }

    @Override
    public void callAfter(String pageType) {
        XennioLogger.log("Trying to get xenn in-app notification");
        requestParameters.put("sdkKey", xennConfig.getSdkKey());
        requestParameters.put("pid", applicationContextHolder.getPersistentId());
        if (sessionContextHolder.getMemberId() != null) {
            requestParameters.put("memberId", sessionContextHolder.getMemberId());
        }
        if(pageType!=null){
            requestParameters.put("pageType", pageType);
        }
        ResultConsumer<InAppNotificationResponse> callback = new ResultConsumer<InAppNotificationResponse>() {
            @Override
            public void consume(InAppNotificationResponse data) {
                showInAppNotification(data);
            }
        };
        httpService.getApiRequest("/in-app-notifications", requestParameters, new ResponseBodyHandler<InAppNotificationResponse>() {
            @Override
            public InAppNotificationResponse handle(String rawResponseBody) {
                return InAppNotificationResponse.fromMap(jsonDeserializerService.deserializeToMap(rawResponseBody));
            }
        }, callback);
    }
}
