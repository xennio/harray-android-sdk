package io.xenn.android.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class EncodingServiceTest {

    @Test
    public void it_should_convert_url_encoded_version_of_given_text() throws Exception {
        String text = "http://www.xenn.io?foo=bar&a=b+c";
        EncodingService encodingService = new EncodingService();
        String encodedValue = encodingService.getUrlEncodedString(text);

        assertEquals("http%3A%2F%2Fwww.xenn.io%3Ffoo%3Dbar%26a%3Db%2Bc", encodedValue);
    }

    @Test
    public void it_should_encode_string_to_base_64_string() {
        String value = "plain text value";
        EncodingService encodingService = new EncodingService();
        String result = encodingService.getBase64EncodedString(value);
        assertEquals("cGxhaW4gdGV4dCB2YWx1ZQ==\n", result);
    }
}