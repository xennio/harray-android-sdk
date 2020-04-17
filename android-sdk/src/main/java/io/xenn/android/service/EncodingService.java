package io.xenn.android.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EncodingService {
    public String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }
}
