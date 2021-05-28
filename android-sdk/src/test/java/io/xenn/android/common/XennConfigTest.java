package io.xenn.android.common;

import org.junit.Test;

import java.util.Collections;

import io.xenn.android.model.TestXennPlugin;

import static org.junit.Assert.assertEquals;

public class XennConfigTest {

    @Test
    public void it_should_build_xennConfig_with_mandatory_fields() {
        XennConfig xennConfig = XennConfig.init("sdkKey");

        assertEquals("https://api.xenn.io:443", xennConfig.getApiUrl());
        assertEquals("sdkKey", xennConfig.getSdkKey());
        assertEquals("https://c.xenn.io:443", xennConfig.getCollectorUrl());
        assertEquals(Collections.emptyList(), xennConfig.getXennPlugins());
    }

    @Test
    public void it_should_build_xennConfig_with_custom_apiUrl() {
        XennConfig xennConfig = XennConfig.init("sdkKey")
                .apiUrl("https://custom.xenn.io:443")
                .collectorUrl("https://customcollector.xenn.io:443");

        assertEquals("https://custom.xenn.io:443", xennConfig.getApiUrl());
        assertEquals("sdkKey", xennConfig.getSdkKey());
        assertEquals("https://customcollector.xenn.io:443", xennConfig.getCollectorUrl());
        assertEquals(Collections.emptyList(), xennConfig.getXennPlugins());
    }

    @Test
    public void it_should_build_xennConfig_with_xennPlugin() {
        XennConfig xennConfig = XennConfig.init("sdkKey")
                .useXennPlugin(TestXennPlugin.class);

        assertEquals("https://api.xenn.io:443", xennConfig.getApiUrl());
        assertEquals("sdkKey", xennConfig.getSdkKey());
        assertEquals("https://c.xenn.io:443", xennConfig.getCollectorUrl());
        assertEquals(Collections.singletonList(TestXennPlugin.class), xennConfig.getXennPlugins());
    }

    @Test
    public void it_should_build_xennConfig_without_xennPlugin_when_plugin_is_null() {
        XennConfig xennConfig = XennConfig.init("sdkKey")
                .useXennPlugin(null);

        assertEquals("https://api.xenn.io:443", xennConfig.getApiUrl());
        assertEquals("sdkKey", xennConfig.getSdkKey());
        assertEquals("https://c.xenn.io:443", xennConfig.getCollectorUrl());
        assertEquals(Collections.emptyList(), xennConfig.getXennPlugins());
    }

    @Test
    public void it_should_build_xennConfig_with_only_single_xennPlugin_when_recurring_add() {
        XennConfig xennConfig = XennConfig.init("sdkKey")
                .useXennPlugin(TestXennPlugin.class)
                .useXennPlugin(TestXennPlugin.class)
                .useXennPlugin(TestXennPlugin.class);

        assertEquals("https://api.xenn.io:443", xennConfig.getApiUrl());
        assertEquals("sdkKey", xennConfig.getSdkKey());
        assertEquals("https://c.xenn.io:443", xennConfig.getCollectorUrl());
        assertEquals(Collections.singletonList(TestXennPlugin.class), xennConfig.getXennPlugins());
    }
}