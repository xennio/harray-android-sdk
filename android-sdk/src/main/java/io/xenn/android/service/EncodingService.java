package io.xenn.android.service;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EncodingService {
    public String getUrlEncodedString(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }

    public String getBase64EncodedString(String value) {
        return Base64.encodeToString(value.getBytes(), Base64.DEFAULT);
    }
}
