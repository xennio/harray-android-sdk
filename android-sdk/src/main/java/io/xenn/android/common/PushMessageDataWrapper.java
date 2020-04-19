package io.xenn.android.common;

import java.util.Map;

public class PushMessageDataWrapper {

    private Map<String, String> data;

    public static PushMessageDataWrapper from(Map<String, String> data) {
        PushMessageDataWrapper pushMessageDataWrapper = new PushMessageDataWrapper();
        pushMessageDataWrapper.data = data;
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
}
