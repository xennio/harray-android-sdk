package io.xenn.android.utils;

import java.net.MalformedURLException;
import java.net.URL;

public final class UrlUtils {

    public static String appendPath(String url, String path) {
        if (path.startsWith("/")) {
            return url + path;
        }
        return url + "/" + path;
    }

    public static String getValidUrl(String url) {
        validateUrl(url);
        return removeTrailingSlash(url);
    }

    private static void validateUrl(String url) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid url: " + url + ". Please be sure the url is in valid format");
        }
    }

    private static String removeTrailingSlash(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.lastIndexOf("/"));
        }
        return url;
    }
}
