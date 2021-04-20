package io.xenn.android.common;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.xenn.android.context.XennPlugin;
import io.xenn.android.utils.UrlUtils;

public final class XennConfig {

    private final String sdkKey;
    private final String collectorUrl;
    private String apiUrl = Constants.XENN_API_URL;
    private List<Class<? extends XennPlugin>> xennPlugins = new ArrayList<>();

    private XennConfig(String sdkKey, String collectorUrl) {
        this.sdkKey = sdkKey;
        this.collectorUrl = collectorUrl;
    }

    public static XennConfig init(String sdkKey, String collectorUrl) {
        return new XennConfig(sdkKey, UrlUtils.getValidUrl(collectorUrl));
    }

    public XennConfig apiUrl(String apiUrl) {
        this.apiUrl = UrlUtils.getValidUrl(apiUrl);
        return this;
    }

    public XennConfig useXennPlugin(Class<? extends XennPlugin> xennPlugin) {
        if (xennPlugin == null) {
            return this;
        }
        if (!this.xennPlugins.contains(xennPlugin)) {
            this.xennPlugins.add(xennPlugin);
        }
        return this;
    }

    public String getSdkKey() {
        return sdkKey;
    }

    public String getCollectorUrl() {
        return collectorUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public List<Class<? extends XennPlugin>> getXennPlugins() {
        return xennPlugins;
    }
}
