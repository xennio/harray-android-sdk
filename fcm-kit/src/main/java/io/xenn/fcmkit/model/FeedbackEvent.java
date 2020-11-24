package io.xenn.fcmkit.model;

import java.util.HashMap;
import java.util.Map;

public class FeedbackEvent {

    private final String type;
    private final String pushId;
    private final String campaignId;
    private final String campaignDate;

    public FeedbackEvent(String type, String pushId, String campaignId, String campaignDate) {
        this.type = type;
        this.pushId = pushId;
        this.campaignId = campaignId;
        this.campaignDate = campaignDate;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> feedbackEvent = new HashMap<>();
        feedbackEvent.put("n", type);
        feedbackEvent.put("pi", pushId);
        feedbackEvent.put("ci", campaignId);
        feedbackEvent.put("cd", campaignDate);
        return feedbackEvent;
    }
}
