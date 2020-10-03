package io.xenn.android;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import io.xenn.android.common.Constants;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.context.SessionState;
import io.xenn.android.event.EventProcessorHandler;
import io.xenn.android.event.SDKEventProcessorHandler;
import io.xenn.android.http.HttpRequestFactory;
import io.xenn.android.notification.NotificationProcessorHandler;
import io.xenn.android.service.DeviceService;
import io.xenn.android.service.EncodingService;
import io.xenn.android.service.EntitySerializerService;
import io.xenn.android.service.HttpService;
import io.xenn.android.service.JsonSerializerService;

public final class Xennio {

    protected EventProcessorHandler eventProcessorHandler;
    protected SDKEventProcessorHandler sdkEventProcessorHandler;
    protected SessionContextHolder sessionContextHolder;
    protected ApplicationContextHolder applicationContextHolder;
    protected NotificationProcessorHandler notificationProcessorHandler;
    protected String pushNotificationToken = "";

    private static Xennio instance;

    private Xennio(Context context, String sdkKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE);
        this.applicationContextHolder = new ApplicationContextHolder(sharedPreferences);
        sessionContextHolder = new SessionContextHolder();

        HttpService httpService = new HttpService(new HttpRequestFactory(), sdkKey);
        EntitySerializerService entitySerializerService = new EntitySerializerService(new EncodingService(), new JsonSerializerService());
        this.eventProcessorHandler = new EventProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, entitySerializerService);

        DeviceService deviceService = new DeviceService(context);
        this.sdkEventProcessorHandler = new SDKEventProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, entitySerializerService, deviceService);

        this.notificationProcessorHandler = new NotificationProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, entitySerializerService, deviceService);

    }

    public static void configure(Context context, String sdkKey) {
        instance = new Xennio(context, sdkKey);
        instance.notificationProcessorHandler.resetBadgeCounts(context);
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

    public static NotificationProcessorHandler notifications() {
        return getInstance().notificationProcessorHandler;
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
            if (!"".equals(instance.pushNotificationToken)) {
                instance.notificationProcessorHandler.savePushToken(instance.pushNotificationToken);
            }
        }
    }

    public static void savePushToken(String deviceToken) {
        Xennio instance = getInstance();
        instance.pushNotificationToken = deviceToken;
        instance.notificationProcessorHandler.savePushToken(deviceToken);
    }

    public static void logout() {
        Xennio instance = getInstance();
        instance.notificationProcessorHandler.removeTokenAssociation(instance.pushNotificationToken);
        instance.pushNotificationToken = "";
        instance.sessionContextHolder.logout();
    }
}