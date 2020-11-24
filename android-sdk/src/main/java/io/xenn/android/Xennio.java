package io.xenn.android;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.Map;

import io.xenn.android.common.Constants;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.context.SessionState;
import io.xenn.android.context.XennPlugin;
import io.xenn.android.context.XennPluginRegistry;
import io.xenn.android.event.EcommerceEventProcessorHandler;
import io.xenn.android.event.EventProcessorHandler;
import io.xenn.android.event.SDKEventProcessorHandler;
import io.xenn.android.http.HttpRequestFactory;
import io.xenn.android.service.DeviceService;
import io.xenn.android.service.EncodingService;
import io.xenn.android.service.EntitySerializerService;
import io.xenn.android.service.HttpService;
import io.xenn.android.service.JsonSerializerService;

public final class Xennio {

    private EntitySerializerService entitySerializerService;
    protected EventProcessorHandler eventProcessorHandler;
    protected SDKEventProcessorHandler sdkEventProcessorHandler;
    protected SessionContextHolder sessionContextHolder;
    protected ApplicationContextHolder applicationContextHolder;
    protected EcommerceEventProcessorHandler ecommerceEventProcessorHandler;
    protected HttpService httpService;
    protected DeviceService deviceService;
    protected XennPluginRegistry xennPluginRegistry;

    private static Xennio instance;

    private Xennio(Context context, String sdkKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE);
        this.applicationContextHolder = new ApplicationContextHolder(sharedPreferences);
        this.sessionContextHolder = new SessionContextHolder();

        this.httpService = new HttpService(new HttpRequestFactory(), sdkKey);
        this.entitySerializerService = new EntitySerializerService(new EncodingService(), new JsonSerializerService());
        EventProcessorHandler eventProcessorHandler = new EventProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, entitySerializerService);
        this.eventProcessorHandler = eventProcessorHandler;

        this.deviceService = new DeviceService(context);
        this.sdkEventProcessorHandler = new SDKEventProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, entitySerializerService, deviceService);

        this.ecommerceEventProcessorHandler = new EcommerceEventProcessorHandler(eventProcessorHandler);

        this.xennPluginRegistry = new XennPluginRegistry();
    }

    @SafeVarargs
    public static void configure(Context context, String sdkKey, Class<? extends XennPlugin>... xennPlugins) {
        instance = new Xennio(context, sdkKey);
        plugins().initAll(Arrays.asList(xennPlugins));
        plugins().onCreate(context);
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

    public static XennPluginRegistry plugins() {
        return getInstance().xennPluginRegistry;
    }

    public static void synchronizeIntentData(Map<String, Object> intentData) {
        getInstance().sessionContextHolder.updateExternalParameters(intentData);
    }

    protected static Xennio getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Xennio.configure(Context context, String sdkKey) must be called before getting instance");
        }
        return instance;
    }

    public static void login(String memberId) {
        if (!"".equals(memberId)) {
            Xennio instance = getInstance();
            instance.sessionContextHolder.login(memberId);
            instance.xennPluginRegistry.onLogin();
        }
    }

    public static void logout() {
        Xennio instance = getInstance();
        instance.xennPluginRegistry.onLogout();
        instance.sessionContextHolder.logout();
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
}