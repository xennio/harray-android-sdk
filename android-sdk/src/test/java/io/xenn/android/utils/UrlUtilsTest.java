package io.xenn.android.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UrlUtilsTest {

    @Test
    public void it_should_throw_error_if_collectorUrl_is_not_valid() {
        try {
            UrlUtils.validateCollectorUrl("https://c.xenn.io:ABC/");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Invalid collectorUrl: https://c.xenn.io:ABC/. Please be sure the url is valid");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void it_should_correct_collectUrl_if_needed() {
        String validUrl = UrlUtils.validateCollectorUrl("https://c.xenn.io:443");
        assertEquals("https://c.xenn.io:443/", validUrl);
    }

    @Test
    public void it_should_not_correct_collectUrl_if_no_needed() {
        String validUrl = UrlUtils.validateCollectorUrl("https://c.xenn.io:443/");
        assertEquals("https://c.xenn.io:443/", validUrl);
    }
}