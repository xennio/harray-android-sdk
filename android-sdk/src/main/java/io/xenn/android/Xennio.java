package io.xenn.android;

import android.content.Context;
import android.content.SharedPreferences;

import io.xenn.android.common.Constants;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
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

    private final EventProcessorHandler eventProcessorHandler;
    private final SDKEventProcessorHandler sdkEventProcessorHandler;
    private final SessionContextHolder sessionContextHolder;
    private final NotificationProcessorHandler notificationProcessorHandler;

    private static Xennio instance;

    private Xennio(Context context, String sdkKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE);
        ApplicationContextHolder applicationContextHolder = new ApplicationContextHolder(sharedPreferences, sdkKey);
        sessionContextHolder = new SessionContextHolder();

        HttpService httpService = new HttpService(applicationContextHolder.getCollectorUrl(), new HttpRequestFactory());
        EntitySerializerService entitySerializerService = new EntitySerializerService(new EncodingService(), new JsonSerializerService());
        this.eventProcessorHandler = new EventProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, entitySerializerService);

        DeviceService deviceService = new DeviceService(context);
        this.sdkEventProcessorHandler = new SDKEventProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, entitySerializerService, deviceService);

        this.notificationProcessorHandler = new NotificationProcessorHandler(applicationContextHolder, sessionContextHolder, httpService, entitySerializerService);
    }

    public static void configure(Context context, String sdkKey) {
        instance = new Xennio(context, sdkKey);
        getInstance().sdkEventProcessorHandler.sessionStart();
    }

    public static EventProcessorHandler eventing() {
        return getInstance().eventProcessorHandler;
    }

    public static NotificationProcessorHandler notifications() {
        return getInstance().notificationProcessorHandler;
    }

    public static Xennio getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Xennio.configure(Context context, String sdkKey) must be called before getting instance");
        }
        return instance;
    }

    public static void login(String memberId) {
        getInstance().sessionContextHolder.login(memberId);
    }
}