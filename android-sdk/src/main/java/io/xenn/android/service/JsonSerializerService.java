package io.xenn.android.service;


import org.json.JSONObject;

import java.util.Map;

public class JsonSerializerService {
    public String serialize(Map<String, Object> value) {
        return new JSONObject(value).toString();
    }
}
