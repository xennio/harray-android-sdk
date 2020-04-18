package io.xenn.android.context;

import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.xenn.android.common.Constants;
import io.xenn.android.utils.RandomValueUtils;

public class ApplicationContextHolder {

    private final String collectorUrl = "https://c.xenn.io:443/";
    private final String sdkVersion = "2.2";
    private final String sdkKey;
    private String persistentId;

    public ApplicationContextHolder(SharedPreferences sharedPreferences, String sdkKey) {
        String value = sharedPreferences.getString(Constants.SDK_PERSISTENT_ID_KEY, null);
        if (value == null) {
            value = RandomValueUtils.randomUUID();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.SDK_PERSISTENT_ID_KEY, value);
            editor.apply();
        }
        this.persistentId = value;
        this.sdkKey = sdkKey;
    }

    public String getTimezone() {
        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        return Long.toString(TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS));
    }

    public String getPersistentId() {
        return persistentId;
    }

    public String getCollectorUrl() {
        return collectorUrl + sdkKey;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

}
