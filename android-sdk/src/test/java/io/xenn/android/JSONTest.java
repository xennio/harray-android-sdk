package io.xenn.android;

import org.junit.Test;

import io.xenn.android.model.XennEvent;

import static org.junit.Assert.assertEquals;

public class JSONTest {
    @Test
    public void valid_json() {
        XennEvent model = new XennEvent()
        .name("HB")
        .addHeader("s", "sid-1")
        .addHeader("p", "pid-1");

        assertEquals("{\"h\":{\"p\":\"pid-1\",\"n\":\"HB\",\"s\":\"sid-1\"},\"b\":{}}", model.toJSON().toString());
    }
}
