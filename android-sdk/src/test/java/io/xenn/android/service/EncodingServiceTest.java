package io.xenn.android.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class EncodingServiceTest {

    @Test
    public void it_should_convert_url_encoded_version_of_given_text() throws Exception {
        String text = "http://www.xenn.io?foo=bar&a=b+c";
        EncodingService encodingService = new EncodingService();
        String encodedValue = encodingService.encode(text);

        assertEquals(encodedValue, "http%3A%2F%2Fwww.xenn.io%3Ffoo%3Dbar%26a%3Db%2Bc");
    }

}