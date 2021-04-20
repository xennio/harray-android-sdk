package io.xenn.android.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class UrlUtilsTest {

    @Test
    public void it_should_throw_error_if_url_is_not_valid() {
        try {
            UrlUtils.getValidUrl("https://c.xenn.io:ABC/");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(e.getMessage(), "Invalid url: https://c.xenn.io:ABC/. Please be sure the url is in valid format");
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void it_should_get_url_as_valid_form() {
        Assert.assertEquals("https://c.xenn.io:443", UrlUtils.getValidUrl("https://c.xenn.io:443/"));
        Assert.assertEquals("https://c.xenn.io:443", UrlUtils.getValidUrl("https://c.xenn.io:443"));

    }

    @Test
    public void it_should_append_path_to_url() {
        Assert.assertEquals("https://a.xenn.io/path", UrlUtils.appendPath("https://a.xenn.io", "/path"));
        Assert.assertEquals("https://a.xenn.io/path", UrlUtils.appendPath("https://a.xenn.io", "path"));
    }
}