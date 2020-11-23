package io.xenn.android.context;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XennPlugins {

    private Map<Class<? extends XennPlugin>, XennPlugin> pluginMap = new HashMap<>();

    public XennPlugins() {
    }

    public <T extends XennPlugin> T get(Class<T> type) {
        return type.cast(pluginMap.get(type));
    }

    public void addAll(List<? extends XennPlugin> xennPlugins) {
        for (XennPlugin xennPlugin : xennPlugins) {
            pluginMap.put(xennPlugin.getClass(), xennPlugin);
        }
    }

    public void initAll(Context context) {
        for (Map.Entry<Class<? extends XennPlugin>, XennPlugin> entry : pluginMap.entrySet()) {
            entry.getValue().init(context);
        }
    }
}