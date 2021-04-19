package io.xenn.android.utils;

import java.net.MalformedURLException;
import java.net.URL;

public final class UrlUtils {

    public static final String XENN_API_URL = "https://api.xenn.io:443";

    public static String validateCollectorUrl(String collectorUrl) {
        try {
            new URL(collectorUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid collectorUrl: " + collectorUrl + ". Please be sure the url is valid");
        }
        if (!collectorUrl.endsWith("/")) {
            return collectorUrl + "/";
        } else {
            return collectorUrl;
        }
    }
}
