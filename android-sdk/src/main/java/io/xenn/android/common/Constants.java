package io.xenn.android.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Constants {
    public static final String PUSH_CHANNEL_ID = "xennio";
    public static final String PUSH_CHANNEL_NAME = "Xenn IO Channel";
    public static final String PUSH_CHANNEL_DESCRIPTION = "Channel for xenn.io push message notification";
    public static final String PUSH_PAYLOAD_SOUND = "sound";
    public static final String PUSH_PAYLOAD_TITLE = "title";
    public static final String PUSH_PAYLOAD_MESSAGE = "message";
    public static final String LOG_TAG = "Xennio";
    public static final String PUSH_PAYLOAD_BADGE = "badge";
    public static final String PUSH_PAYLOAD_IMAGE_URL = "image_url";
    public static final String PUSH_PAYLOAD_SOURCE = "source";
    public static final String SDK_PERSISTENT_ID_KEY = "pid";
    public static final String PREF_COLLECTION_NAME = "XENNIO_PREFS";
    public static final long SESSION_DURATION = 30 * 60 * 1000L;
    public static final List<String> EXTERNAL_PARAMETER_KEYS = Collections.unmodifiableList(Arrays.asList("campaignId", "campaignDate", "pushId", "url", "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content"));
    public static final String PUSH_PAYLOAD_APPLICATION_LOGO = "logo";
    public static final String PUSH_PAYLOAD_SUB_TITLE = "subTitle";
    public static final String PUSH_ID_KEY = "pushId";
    public static final String CAMPAIGN_ID_KEY = "campaignId";
    public static final String CAMPAIGN_DATE_KEY = "campaignDate";
    public static final String PUSH_FEED_BACK_PATH = "feedback";
}
