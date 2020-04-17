package io.xenn.android;

import android.content.Context;
import android.content.SharedPreferences;

import io.xenn.android.common.Constants;
import io.xenn.android.context.ApplicationContextHolder;

public final class Xennio {

    private Context context;
    private SharedPreferences sharedPreferences;
    private final String sdkKey;
    private final ApplicationContextHolder applicationContextHolder;

    public static Xennio instance;


    private Xennio(Context context, String sdkKey) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE);
        this.sdkKey = sdkKey;
        this.applicationContextHolder = new ApplicationContextHolder(this.sharedPreferences);
    }

    public static void configure(Context context, String sdkKey) {
        instance = new Xennio(context, sdkKey);
    }

    public static Xennio getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Xennio.configure(Context context, String sdkKey) must be called before getting instance");
        }
        return instance;
    }

}
