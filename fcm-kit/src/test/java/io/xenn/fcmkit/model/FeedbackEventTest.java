package io.xenn.fcmkit.model;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FeedbackEventTest {

    @Test
    public void it_should_create_map_from_fields() {
        FeedbackEvent feedbackEvent = new FeedbackEvent("o", "pushId", "campaignId", "campaignDate");
        Map<String, Object> result = feedbackEvent.toMap();

        assertEquals("o", result.get("n"));
        assertEquals("pushId", result.get("pi"));
        assertEquals("campaignId", result.get("ci"));
        assertEquals("campaignDate", result.get("cd"));

    }

}