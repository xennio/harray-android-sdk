package io.xenn.android.model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class XennEvent {

    private final Map<String, Object> h = new HashMap<>();
    private final Map<String, Object> b = new HashMap<>();
    private String name;

    private XennEvent(String name) {
        this.name = name;
    }

    public static XennEvent create(String name) {
        XennEvent xennEvent = new XennEvent(name);
        xennEvent.h.put("n", name);
        return xennEvent;
    }

    public static XennEvent create(String name, String persistentId, String sessionId) {
        XennEvent xennEvent = new XennEvent(name);
        xennEvent.h.put("n", name);
        xennEvent.h.put("p", persistentId);
        xennEvent.h.put("s", sessionId);
        return xennEvent;
    }

    public XennEvent addHeader(String key, Object value) {
        h.put(key, value);
        return this;
    }

    public XennEvent addBody(String key, Object value) {
        b.put(key, value);
        return this;
    }


    public JSONObject toJSON() {
        Map<String, JSONObject> xennEvent = new HashMap<>();
        xennEvent.put("h", new JSONObject(h));
        xennEvent.put("b", new JSONObject(b));
        return new JSONObject(xennEvent);
    }

    public XennEvent memberId(String memberId) {
        if (memberId != null && !"".equalsIgnoreCase(memberId)) {
            this.addBody("memberId", memberId);
        }
        return this;
    }

    public XennEvent appendExtra(Map<String, Object> params) {
        if (params != null) {
            this.b.putAll(params);
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("h", h);
        map.put("b", b);
        return map;
    }
}
