package io.xenn.android.context;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

import io.xenn.android.model.TestXennPlugin;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class XennPluginRegistryTest {

    @Test
    public void it_should_get_plugin_from_plugin_registry() {
        XennPluginRegistry reg = new XennPluginRegistry();

        reg.initAll(Collections.<Class<? extends XennPlugin>>singletonList(TestXennPlugin.class));

        TestXennPlugin testXennPlugin = reg.get(TestXennPlugin.class);

        Assert.assertTrue(testXennPlugin instanceof TestXennPlugin);
    }

    @Test
    public void it_should_fail_when_init_plugin_registry_with_plugin_that_has_not_no_arg_constructor() {
        XennPluginRegistry reg = new XennPluginRegistry();
        try {
            reg.initAll(Collections.<Class<? extends XennPlugin>>singletonList(MissingNoArgCtor.class));
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Default no-arg constructor must be exists on the xenn plugin", e.getMessage());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void it_should_fail_when_init_plugin_registry_with_plugin_that_with_failed_constructor() {
        XennPluginRegistry reg = new XennPluginRegistry();
        try {
            reg.initAll(Collections.<Class<? extends XennPlugin>>singletonList(FailedCtor.class));
            fail();
        } catch (Exception e) {
            Assert.assertEquals("Plugin initialization error", e.getMessage());
        }
    }

    public static class MissingNoArgCtor extends XennPlugin {

        public MissingNoArgCtor(String value) {
        }
    }

    public static class FailedCtor extends XennPlugin {

        public FailedCtor() {
            throw new RuntimeException("plugin init fail");
        }
    }
}