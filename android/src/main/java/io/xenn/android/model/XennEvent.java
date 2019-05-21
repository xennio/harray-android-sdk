package io.xenn.android.model;

import java.util.HashMap;
import java.util.Map;

public class XennEvent {

    private final Map<String, Object> h = new HashMap<>();
    private final Map<String, Object> b = new HashMap<>();

    public XennEvent name(String name) {
        h.put("n", name);
        return this;
    }

    public XennEvent addHeader(String key, Object value) {
        h.put(key, value);
        return this;
    }

    public XennEvent addBody(String key, Object value) {
        b.put(key, value);
        return this;
    }


    public Map<String, Object> toMap() {
        Map<String, Object> xennEvent = new HashMap<>();
        xennEvent.put("h", h);
        xennEvent.put("b", b);
        return xennEvent;
    }

    public XennEvent memberId(String memberId) {
        if (memberId != null && !"".equalsIgnoreCase(memberId)) {
            this.addBody("memberId", memberId);
        }
        return this;
    }

    public XennEvent appendExtra(Map<String, Object> params) {
        this.b.putAll(params);
        return this;
    }
}
