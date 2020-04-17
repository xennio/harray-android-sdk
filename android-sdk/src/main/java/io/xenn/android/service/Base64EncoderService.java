package io.xenn.android.service;

import android.util.Base64;

public class Base64EncoderService {
    public String encode(String value) {
        return Base64.encodeToString(value.getBytes(), Base64.DEFAULT);
    }
}
