package io.xenn.android.model.inappnotification;

import org.bouncycastle.crypto.tls.HashAlgorithm;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InAppNotificationResponseTest {

    @Test
    public void it_should_convert_response_from_map() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "idValue");
        map.put("html", "htmlValue");
        map.put("style", "styleValue");
        map.put("imageUrl", "imageUrlValue");

        InAppNotificationResponse inAppNotificationResponse = InAppNotificationResponse.fromMap(map);

        assertEquals(inAppNotificationResponse.getHtml(), "htmlValue");
        assertEquals(inAppNotificationResponse.getId(), "idValue");
        assertEquals(inAppNotificationResponse.getStyle(), "styleValue");
        assertEquals(inAppNotificationResponse.getImageUrl(), "imageUrlValue");
    }

    @Test
    public void it_should_convert_null_response_from_empty_map() {
        Map<String, String> emptyMap = new HashMap<>();

        InAppNotificationResponse inAppNotificationResponse = InAppNotificationResponse.fromMap(emptyMap);

        assertNull(inAppNotificationResponse);
    }
}