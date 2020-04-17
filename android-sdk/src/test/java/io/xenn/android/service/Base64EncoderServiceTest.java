package io.xenn.android.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class Base64EncoderServiceTest {

    @Test
    public void it_should_encode_string_to_base_64_string() {
        String value = "plain text value";
        Base64EncoderService base64EncoderService = new Base64EncoderService();
        String result = base64EncoderService.encode(value);
        assertEquals(result, "cGxhaW4gdGV4dCB2YWx1ZQ==\n");
    }

}