package io.xenn.hmskit.common;

import android.content.Intent;
import android.os.Bundle;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PushMessageDataWrapperTest {

    @Test
    public void it_should_build_channel_id_with_out_sound_when_push_message_does_not_contain_sound() {
        Map<String, String> message = new HashMap<>();
        assertEquals("xennio", PushMessageDataWrapper.from(message).buildChannelId());
    }

    @Test
    public void it_should_build_channel_id_with_sound_when_push_message_contains_sound() {
        Map<String, String> message = new HashMap<>();
        message.put(Constants.PUSH_PAYLOAD_SOUND, "sound");
        assertEquals("xenniosound", PushMessageDataWrapper.from(message).buildChannelId());
    }

    @Test
    public void it_should_return_false_when_push_message_does_not_contain_sound() {
        Map<String, String> message = new HashMap<>();
        assertFalse(PushMessageDataWrapper.from(message).hasSound());
    }

    @Test
    public void it_should_return_sound_when_push_message_contains_sound() {
        Map<String, String> message = new HashMap<>();
        message.put(Constants.PUSH_PAYLOAD_SOUND, "sound");
        assertEquals("sound", PushMessageDataWrapper.from(message).getSound());
    }

    @Test
    public void it_should_return_true_when_push_message_is_silent() {
        Map<String, String> message = new HashMap<>();
        assertTrue(PushMessageDataWrapper.from(message).isSilent());
    }

    @Test
    public void it_should_return_zero_when_push_message_does_not_contain_badge() {
        Map<String, String> message = new HashMap<>();
        assertEquals(0, PushMessageDataWrapper.from(message).getBadge());
    }

    @Test
    public void it_should_return_zero_when_push_message_contains_badge_but_not_numeric() {
        Map<String, String> message = new HashMap<>();
        message.put(Constants.PUSH_PAYLOAD_BADGE, "asdasd");
        assertEquals(0, PushMessageDataWrapper.from(message).getBadge());
    }

    @Test
    public void it_should_return_badge_value_when_push_message_contains_badge() {
        Map<String, String> message = new HashMap<>();
        message.put(Constants.PUSH_PAYLOAD_BADGE, "12");
        assertEquals(12, PushMessageDataWrapper.from(message).getBadge());
    }

    @Test
    public void it_should_return_image_url_value_when_push_message_contains_image_url() {
        Map<String, String> message = new HashMap<>();
        message.put(Constants.PUSH_PAYLOAD_IMAGE_URL, "http://imageurl");
        assertEquals("http://imageurl", PushMessageDataWrapper.from(message).getImageUrl());
    }

    @Test
    public void it_should_return_badge_value_when_push_message_contains_title() {
        Map<String, String> message = new HashMap<>();
        message.put(Constants.PUSH_PAYLOAD_TITLE, "message title");
        assertEquals("message title", PushMessageDataWrapper.from(message).getTitle());
    }

    @Test
    public void it_should_return_badge_value_when_push_message_contains_message() {
        Map<String, String> message = new HashMap<>();
        message.put(Constants.PUSH_PAYLOAD_MESSAGE, "message content");
        assertEquals("message content", PushMessageDataWrapper.from(message).getMessage());
    }

    @Test
    public void it_should_return_logo_url_value_when_push_message_contains_logo() {
        Map<String, String> message = new HashMap<>();
        message.put(Constants.PUSH_PAYLOAD_APPLICATION_LOGO, "http://imageurl");
        assertEquals("http://imageurl", PushMessageDataWrapper.from(message).getApplicationLogo());
    }

    @Test
    public void it_should_return_source_value_when_push_message_contains_source() {
        Map<String, String> message = new HashMap<>();
        message.put(Constants.PUSH_PAYLOAD_SOURCE, "xennio");
        assertEquals("xennio", PushMessageDataWrapper.from(message).getSource());
    }

    @Test
    public void it_should_convert_string_value_type_map_to_object_map() {
        Map<String, String> message = new HashMap<>();
        message.put("foo", "bar");
        message.put("foo1", "bar2");
        Map<String, Object> expected = PushMessageDataWrapper.from(message).toObjectMap();
        assertTrue(message.size() == expected.size());
        assertTrue(expected.containsKey("foo"));
        assertTrue(expected.containsKey("foo1"));
    }

    @Test
    public void it_should_wrap_push_notification_data_from_intent() {
        Map<String, String> message = new HashMap<>();
        message.put("pushId", "pushId");
        message.put("campaignDate", "campaignDate");
        message.put("campaignId", "campaignId");

        PushMessageDataWrapper pushMessageDataWrapper = PushMessageDataWrapper.from(message);

        assertEquals("pushId", pushMessageDataWrapper.getPushId());
        assertEquals("campaignDate", pushMessageDataWrapper.getCampaignDate());
        assertEquals("campaignId", pushMessageDataWrapper.getCampaignId());
    }

    @Test
    public void it_should_put_all_bundle_elements_to_map() {
        Intent intent = mock(Intent.class);
        Bundle bundle = mock(Bundle.class);
        when(intent.getExtras()).thenReturn(bundle);
        Set<String> bundleKeys = new HashSet<>();
        bundleKeys.add("pushId");
        bundleKeys.add("campaignDate");
        bundleKeys.add("campaignId");
        when(bundle.keySet()).thenReturn(bundleKeys);
        when(intent.getStringExtra("pushId")).thenReturn("pushId");
        when(intent.getStringExtra("campaignDate")).thenReturn("campaignDate");
        when(intent.getStringExtra("campaignId")).thenReturn("campaignId");

        PushMessageDataWrapper pushMessageDataWrapper = PushMessageDataWrapper.from(intent);

        assertEquals(3, pushMessageDataWrapper.getData().size());
        assertEquals("pushId", pushMessageDataWrapper.getData().get("pushId"));
        assertEquals("campaignDate", pushMessageDataWrapper.getData().get("campaignDate"));
        assertEquals("campaignId", pushMessageDataWrapper.getData().get("campaignId"));
    }

}