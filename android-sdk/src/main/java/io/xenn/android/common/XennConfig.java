package io.xenn.android.common;

import java.util.ArrayList;
import java.util.List;

import io.xenn.android.context.XennPlugin;
import io.xenn.android.event.inappnotification.LinkClickHandler;
import io.xenn.android.model.inappnotification.InAppNotificationHandlerStrategy;
import io.xenn.android.utils.UrlUtils;

public final class XennConfig {

    private final String sdkKey;
    private String collectorUrl = Constants.XENN_COLLECTOR_URL;
    private String apiUrl = Constants.XENN_API_URL;
    private List<Class<? extends XennPlugin>> xennPlugins = new ArrayList<>();
    private LinkClickHandler inAppNotificationLinkClickHandler;
    private InAppNotificationHandlerStrategy inAppNotificationHandlerStrategy = InAppNotificationHandlerStrategy.PageViewEvent;

    private XennConfig(String sdkKey) {
        this.sdkKey = sdkKey;
    }

    public static XennConfig init(String sdkKey) {
        return new XennConfig(sdkKey);
    }

    public XennConfig apiUrl(String apiUrl) {
        this.apiUrl = UrlUtils.getValidUrl(apiUrl);
        return this;
    }

    public XennConfig collectorUrl(String collectorUrl) {
        this.collectorUrl = UrlUtils.getValidUrl(collectorUrl);
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

    public XennConfig inAppNotificationLinkClickHandler(LinkClickHandler linkClickHandler) {
        this.inAppNotificationLinkClickHandler = linkClickHandler;
        return this;
    }

    public XennConfig inAppNotificationHandlerStrategy(InAppNotificationHandlerStrategy inAppNotificationHandlerStrategy) {
        this.inAppNotificationHandlerStrategy = inAppNotificationHandlerStrategy;
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

    public LinkClickHandler getInAppNotificationLinkClickHandler() {
        return inAppNotificationLinkClickHandler;
    }

    public InAppNotificationHandlerStrategy getInAppNotificationHandlerStrategy() {
        return inAppNotificationHandlerStrategy;
    }
}
