package io.xenn.android.model;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class XennEventTest {

    @Test
    public void it_should_construct_json_properly() {
        String eventName = "test";
        XennEvent xennEvent = new XennEvent()
                .name(eventName)
                .addHeader("header2", "value1")
                .addHeader("header1", "value2")
                .addBody("body1", 1)
                .addBody("body2", "stringValue");
        assertEquals("{\"h\":{\"header2\":\"value1\",\"n\":\"test\",\"header1\":\"value2\"},\"b\":{\"body2\":\"stringValue\",\"body1\":1}}", xennEvent.toJSON().toString());
        assertEquals(eventName, xennEvent.getName());
    }

    @Test
    public void it_should_add_all_values_to_body() {
        Map<String, Object> extraParams = new HashMap<>();
        String eventName = "testEvent";
        extraParams.put("body3", "extra1");
        extraParams.put("body4", "extra2");
        XennEvent xennEvent = new XennEvent()
                .name(eventName)
                .addHeader("header2", "value1")
                .addHeader("header1", "value2")
                .addBody("body1", 1)
                .addBody("body2", "stringValue")
                .appendExtra(extraParams);
        assertEquals("{\"h\":{\"header2\":\"value1\",\"n\":\"testEvent\",\"header1\":\"value2\"},\"b\":{\"body2\":\"stringValue\",\"body3\":\"extra1\",\"body4\":\"extra2\",\"body1\":1}}", xennEvent.toJSON().toString());
        assertEquals(eventName, xennEvent.getName());
    }

    @Test
    public void it_should_construct_json_properly_with_memberId() {
        String eventName = "test";
        XennEvent xennEvent = new XennEvent()
                .name(eventName)
                .addHeader("header2", "value1")
                .addHeader("header1", "value2")
                .addBody("body1", 1)
                .addBody("body2", "stringValue")
                .memberId("someMemberId");
        assertEquals("{\"h\":{\"header2\":\"value1\",\"n\":\"test\",\"header1\":\"value2\"},\"b\":{\"body2\":\"stringValue\",\"body1\":1,\"memberId\":\"someMemberId\"}}", xennEvent.toJSON().toString());
        assertEquals(eventName, xennEvent.getName());
    }
}