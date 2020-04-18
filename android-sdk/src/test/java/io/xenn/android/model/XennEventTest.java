package io.xenn.android.model;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class XennEventTest {

    @Test
    public void it_should_construct_fields_properly() {
        String eventName = "test";
        Map<String, Object> extraParams = new HashMap<>();
        extraParams.put("body3", "extra1");
        extraParams.put("body4", "extra2");
        XennEvent xennEvent = XennEvent
                .create(eventName, "pid", "sid")
                .memberId("memberId")
                .addHeader("header2", "value1")
                .addHeader("header1", "value2")
                .addBody("body1", 1)
                .addBody("body2", "stringValue")
                .appendExtra(extraParams);

        Map<String, Object> xennEventMap = xennEvent.toMap();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals(eventName, header.get("n"));
        assertEquals("sid", header.get("s"));
        assertEquals("pid", header.get("p"));
        assertEquals("value1", header.get("header2"));
        assertEquals("value2", header.get("header1"));
        assertEquals(1, body.get("body1"));
        assertEquals("stringValue", body.get("body2"));
        assertEquals("memberId", body.get("memberId"));
        assertEquals("extra1", body.get("body3"));
        assertEquals("extra2", body.get("body4"));

    }

    @Test
    public void it_should_not_add_member_id_when_empty_string_set_to_member_id() {
        String eventName = "test";
        Map<String, Object> extraParams = new HashMap<>();
        extraParams.put("body3", "extra1");
        extraParams.put("body4", "extra2");
        XennEvent xennEvent = XennEvent
                .create(eventName, "pid", "sid")
                .memberId("")
                .addHeader("header2", "value1")
                .addHeader("header1", "value2")
                .addBody("body1", 1)
                .addBody("body2", "stringValue")
                .appendExtra(extraParams);

        Map<String, Object> xennEventMap = xennEvent.toMap();
        Map<String, Object> header = (Map<String, Object>) xennEventMap.get("h");
        Map<String, Object> body = (Map<String, Object>) xennEventMap.get("b");

        assertEquals(eventName, header.get("n"));
        assertEquals("sid", header.get("s"));
        assertEquals("pid", header.get("p"));
        assertEquals("value1", header.get("header2"));
        assertEquals("value2", header.get("header1"));
        assertEquals(1, body.get("body1"));
        assertEquals("stringValue", body.get("body2"));
        assertEquals("extra1", body.get("body3"));
        assertEquals("extra2", body.get("body4"));

        assertNull(body.get("memberId"));

    }
}