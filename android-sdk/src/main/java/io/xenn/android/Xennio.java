package io.xenn.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.Map;

import io.xenn.android.common.Constants;
import io.xenn.android.common.XennConfig;
import io.xenn.android.context.ActivityLifecycleListener;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.context.SessionState;
import io.xenn.android.context.XennPluginRegistry;
import io.xenn.android.event.BrowsingHistoryProcessorHandler;
import io.xenn.android.event.ChainProcessorHandler;
import io.xenn.android.event.EcommerceEventProcessorHandler;
import io.xenn.android.event.EventProcessorHandler;
import io.xenn.android.event.MemberSummaryProcessorHandler;
import io.xenn.android.event.PushMessagesHistoryProcessorHandler;
import io.xenn.android.event.inappnotification.InAppNotificationProcessorHandler;
import io.xenn.android.event.RecommendationProcessorHandler;
import io.xenn.android.event.SDKEventProcessorHandler;
import io.xenn.android.http.HttpRequestFactory;
import io.xenn.android.model.inappnotification.InAppNotificationHandlerStrategy;
import io.xenn.android.service.DeviceService;
import io.xenn.android.service.EncodingService;
import io.xenn.android.service.EntitySerializerService;
import io.xenn.android.service.HttpService;
import io.xenn.android.service.JsonDeserializerService;
import io.xenn.android.service.JsonSerializerService;
import io.xenn.android.utils.XennioLogger;

public final class Xennio {

    private EntitySerializerService entitySerializerService;
    protected EventProcessorHandler eventProcessorHandler;
    protected SDKEventProcessorHandler sdkEventProcessorHandler;
    protected SessionContextHolder sessionContextHolder;
    protected ApplicationContextHolder applicationContextHolder;
    protected EcommerceEventProcessorHandler ecommerceEventProcessorHandler;
    protected RecommendationProcessorHandler recommendationProcessorHandler;
    protected BrowsingHistoryProcessorHandler browsingHistoryProcessorHandler;
    protected InAppNotificationProcessorHandler inAppNotificationProcessorHandler;
    protected PushMessagesHistoryProcessorHandler pushMessagesHistoryProcessorHandler;
    protected MemberSummaryProcessorHandler memberSummaryProcessorHandler;
    protected HttpService httpService;
    protected DeviceService deviceService;
    protected XennPluginRegistry xennPluginRegistry;

    private static Xennio instance;

    private Xennio(Context context, XennConfig xennConfig) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE);
        this.applicationContextHolder = new ApplicationContextHolder(sharedPreferences);
        this.sessionContextHolder = new SessionContextHolder();
        EncodingService encodingService = new EncodingService();

        this.httpService = new HttpService(new HttpRequestFactory(), xennConfig.getSdkKey(), xennConfig.getCollectorUrl(), xennConfig.getApiUrl());
        this.entitySerializerService = new EntitySerializerService(encodingService, new JsonSerializerService());
        ChainProcessorHandler chainProcessorHandler = new ChainProcessorHandler();
        EventProcessorHandler eventProcessorHandler = new EventProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, entitySerializerService, chainProcessorHandler);
        this.eventProcessorHandler = eventProcessorHandler;

        this.deviceService = new DeviceService(context);
        this.sdkEventProcessorHandler = new SDKEventProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, entitySerializerService, deviceService);

        this.ecommerceEventProcessorHandler = new EcommerceEventProcessorHandler(eventProcessorHandler);

        JsonDeserializerService jsonDeserializerService = new JsonDeserializerService();
        this.recommendationProcessorHandler = new RecommendationProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, xennConfig.getSdkKey(), jsonDeserializerService, encodingService);
        this.browsingHistoryProcessorHandler = new BrowsingHistoryProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, xennConfig.getSdkKey(), jsonDeserializerService);
        this.inAppNotificationProcessorHandler = new InAppNotificationProcessorHandler(
                eventProcessorHandler, applicationContextHolder, sessionContextHolder, httpService, jsonDeserializerService, xennConfig);
        this.pushMessagesHistoryProcessorHandler = new PushMessagesHistoryProcessorHandler(sessionContextHolder, httpService, xennConfig.getSdkKey(), jsonDeserializerService);
        this.memberSummaryProcessorHandler = new MemberSummaryProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, xennConfig.getSdkKey(), jsonDeserializerService);
        this.xennPluginRegistry = new XennPluginRegistry();

        if (xennConfig.getInAppNotificationHandlerStrategy() == InAppNotificationHandlerStrategy.PageViewEvent) {
            chainProcessorHandler.addHandler(inAppNotificationProcessorHandler);
        }


    }

    public static void configure(Context context, @NonNull XennConfig xennConfig) {
        instance = new Xennio(context, xennConfig);
        plugins().initAll(xennConfig.getXennPlugins());
        plugins().onCreate(context);
        registerActivityLifecycleListener(context);
    }

    public static EventProcessorHandler eventing() {
        SessionContextHolder sessionContextHolder = getInstance().sessionContextHolder;
        if (sessionContextHolder.getSessionState() != SessionState.SESSION_STARTED) {
            getInstance().sdkEventProcessorHandler.sessionStart();
            sessionContextHolder.startSession();
            if (getInstance().applicationContextHolder.isNewInstallation()) {
                getInstance().sdkEventProcessorHandler.newInstallation();
                getInstance().applicationContextHolder.setInstallationCompleted();
            }

        }
        return getInstance().eventProcessorHandler;
    }

    public static EcommerceEventProcessorHandler ecommerce() {
        return getInstance().ecommerceEventProcessorHandler;
    }

    public static RecommendationProcessorHandler recommendations() {
        return getInstance().recommendationProcessorHandler;
    }

    public static BrowsingHistoryProcessorHandler browsingHistory() {
        return getInstance().browsingHistoryProcessorHandler;
    }

    public static MemberSummaryProcessorHandler memberSummary() {
        return getInstance().memberSummaryProcessorHandler;
    }

    public static InAppNotificationProcessorHandler inAppNotifications() {
        return getInstance().inAppNotificationProcessorHandler;
    }

    public static PushMessagesHistoryProcessorHandler pushMessagesHistory() {
        return getInstance().pushMessagesHistoryProcessorHandler;
    }

    public static XennPluginRegistry plugins() {
        return getInstance().xennPluginRegistry;
    }

    public static void synchronizeIntentData(Map<String, Object> intentData) {
        getInstance().sessionContextHolder.updateExternalParameters(intentData);
    }

    protected static Xennio getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Xennio.configure(Context context, String sdkKey, String collectorUrl) must be called before getting instance");
        }
        return instance;
    }

    public static void login(String memberId) {
        Xennio instance = getInstance();
        if (memberId != null && !"".equals(memberId) && !memberId.equals(instance.sessionContextHolder.getMemberId())) {
            instance.sessionContextHolder.login(memberId);
            instance.sessionContextHolder.restartSession();
            instance.xennPluginRegistry.onLogin();
        }
    }

    public static void logout() {
        Xennio instance = getInstance();
        instance.xennPluginRegistry.onLogout();
        instance.sessionContextHolder.logout();
        instance.sessionContextHolder.restartSession();
    }

    public static EntitySerializerService getEntitySerializerService() {
        return getInstance().entitySerializerService;
    }

    public static ApplicationContextHolder getApplicationContextHolder() {
        return getInstance().applicationContextHolder;
    }

    public static SessionContextHolder getSessionContextHolder() {
        return getInstance().sessionContextHolder;
    }

    public static HttpService getHttpService() {
        return getInstance().httpService;
    }

    public static DeviceService getDeviceService() {
        return getInstance().deviceService;
    }

    private static void registerActivityLifecycleListener(Context context) {
        if (context instanceof Application) {
            ((Application) context).registerActivityLifecycleCallbacks(new ActivityLifecycleListener());
        } else {
            XennioLogger.log("context parameter is not Application type");
        }
    }
}