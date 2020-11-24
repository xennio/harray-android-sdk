package io.xenn.android.context;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XennPluginRegistry {

    private Map<Class<? extends XennPlugin>, XennPlugin> pluginMap = new HashMap<>();

    public XennPluginRegistry() {
    }

    public <T extends XennPlugin> T get(Class<T> type) {
        try {
            return type.cast(pluginMap.get(type));
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("A xenn plugin must 'extends' XennPlugin class", e);
        }
    }

    public void initAll(List<Class<? extends XennPlugin>> xennPlugins) {
        for (Class<? extends XennPlugin> xennPlugin : xennPlugins) {
            try {
                pluginMap.put(xennPlugin, xennPlugin.getConstructor().newInstance());
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("Default no-arg constructor must be exists on the xenn plugin", e);
            } catch (Exception e) {
                throw new IllegalArgumentException("Plugin initialization error", e);
            }
        }
    }

    public void onCreate(Context context) {
        for (Map.Entry<Class<? extends XennPlugin>, XennPlugin> entry : pluginMap.entrySet()) {
            entry.getValue().onCreate(context);
        }
    }

    public void onLogin() {
        for (Map.Entry<Class<? extends XennPlugin>, XennPlugin> entry : pluginMap.entrySet()) {
            entry.getValue().onLogin();
        }
    }

    public void onLogout() {
        for (Map.Entry<Class<? extends XennPlugin>, XennPlugin> entry : pluginMap.entrySet()) {
            entry.getValue().onLogout();
        }
    }
}