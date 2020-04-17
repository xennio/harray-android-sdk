package io.xenn.android.event;

public abstract class EventBase {
    private final String sessionId;
    private final String persistentId;
    private final String eventName;

    protected EventBase(String eventName, String persistentId, String sessionId) {
        this.eventName = eventName;
        this.sessionId = sessionId;
        this.persistentId = persistentId;
    }
}
