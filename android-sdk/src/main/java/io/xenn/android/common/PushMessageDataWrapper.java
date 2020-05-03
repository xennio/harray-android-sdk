package io.xenn.android.common;

import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PushMessageDataWrapper {

    private Map<String, String> data;

    public static PushMessageDataWrapper from(Map<String, String> data) {
        PushMessageDataWrapper pushMessageDataWrapper = new PushMessageDataWrapper();
        pushMessageDataWrapper.data = data;
        return pushMessageDataWrapper;
    }

    public static PushMessageDataWrapper from(Intent intent) {
        PushMessageDataWrapper pushMessageDataWrapper = new PushMessageDataWrapper();
        pushMessageDataWrapper.data = new HashMap<>();
        Bundle extras = intent.getExtras();
        Set<String> ks = extras.keySet();
        for (String key : ks) {
            pushMessageDataWrapper.data.put(key, intent.getStringExtra(key));
        }
        return pushMessageDataWrapper;
    }

    public String buildChannelId() {
        String soundChannelId = this.hasSound() ? this.getSound() : "";
        return Constants.PUSH_CHANNEL_ID + soundChannelId;
    }

    public boolean hasSound() {
        return data.containsKey(Constants.PUSH_PAYLOAD_SOUND);
    }

    public String getSound() {
        return data.get(Constants.PUSH_PAYLOAD_SOUND);
    }

    public String getTitle() {
        return data.get(Constants.PUSH_PAYLOAD_TITLE);
    }

    public String getMessage() {
        return data.get(Constants.PUSH_PAYLOAD_MESSAGE);
    }

    public boolean isSilent() {
        return !data.containsKey(Constants.PUSH_PAYLOAD_TITLE);
    }

    public int getBadge() {
        int badge = 0;
        try {
            if (data.containsKey(Constants.PUSH_PAYLOAD_BADGE)) {
                badge = Integer.parseInt(data.get(Constants.PUSH_PAYLOAD_BADGE));
            }
        } catch (Exception e) {

        }
        return badge;
    }

    public String getImageUrl() {
        return data.get(Constants.PUSH_PAYLOAD_IMAGE_URL);
    }

    public String getApplicationLogo() {
        return data.get(Constants.PUSH_PAYLOAD_APPLICATION_LOGO);
    }

    public String getSubTitle() {
        return data.get(Constants.PUSH_PAYLOAD_SUB_TITLE);
    }

    public Map<String, Object> toObjectMap() {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, String> eachEntry : data.entrySet()) {
            map.put(eachEntry.getKey(), eachEntry.getValue());
        }
        return map;
    }

    public String getPushId() {
        return data.get(Constants.PUSH_ID_KEY);
    }

    public String getCampaignId() {
        return data.get(Constants.CAMPAIGN_ID_KEY);
    }

    public String getCampaignDate() {
        return data.get(Constants.CAMPAIGN_DATE_KEY);
    }

    public Map<String, String> getData() {
        return data;
    }

    public String getSource() {
        return data.get(Constants.PUSH_PAYLOAD_SOURCE);
    }
}
