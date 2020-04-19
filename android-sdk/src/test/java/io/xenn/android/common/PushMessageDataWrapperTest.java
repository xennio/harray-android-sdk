package io.xenn.android.common;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

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

}