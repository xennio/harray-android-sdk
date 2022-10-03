package io.xenn.android.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.xenn.android.utils.XennioLogger;

public class JsonDeserializerService {

    public List<Map<String, String>> deserializeToMapList(String jsonString) {
        if (jsonString == null || jsonString.trim().equals("")) {
            return Collections.emptyList();
        }
        try {
            List<Map<String, String>> result = new ArrayList<>();
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                result.add(jsonObjectToMap(array.getJSONObject(i)));
            }
            return result;
        } catch (JSONException e) {
            XennioLogger.log("Json array deserialize error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Map<String, String> deserializeToMap(String jsonString) {
        if (jsonString == null || jsonString.trim().equals("")) {
            return new HashMap<>();
        }
        try {
            return jsonObjectToMap(new JSONObject(jsonString));
        } catch (JSONException e) {
            XennioLogger.log("Json map deserialize error: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private Map<String, String> jsonObjectToMap(JSONObject jsonObject) {
        try {
            Map<String, String> result = new HashMap<>();
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.get(key).toString();
                result.put(key, value.equals("null") ? null : value);
            }
            return result;
        } catch (JSONException e) {
            XennioLogger.log("Json object deserialize error: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public JSONObject toJsonObject(String rawResponseBody) {
        try{
            return new JSONObject(rawResponseBody);
        }catch (JSONException e){
            XennioLogger.log("Json object deserialize error: " + e.getMessage());
            return null;
        }
    }
}