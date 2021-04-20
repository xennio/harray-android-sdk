package io.xenn.android.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Constants {
    public static final String LOG_TAG = "Xennio";
    public static final String SDK_PERSISTENT_ID_KEY = "pid";
    public static final String PREF_COLLECTION_NAME = "XENNIO_PREFS";
    public static final long SESSION_DURATION = 30 * 60 * 1000L;
    public static final List<String> EXTERNAL_PARAMETER_KEYS = Collections.unmodifiableList(Arrays.asList("campaignId", "campaignDate", "pushId", "url", "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content"));
    public static final String UNKNOWN_PROPERTY_VALUE = "UNKNOWN";
    public static final String ANDROID = "Android";
    public static final String XENN_API_URL = "https://api.xenn.io:443";
}
