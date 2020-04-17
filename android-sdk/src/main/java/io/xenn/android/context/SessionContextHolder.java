package io.xenn.android.context;


import io.xenn.android.common.Constants;
import io.xenn.android.utils.ClockUtils;
import io.xenn.android.utils.RandomValueUtils;

public class SessionContextHolder {

    private String sessionId;
    private long sessionStartTime;
    private long lastActivityTime;


    public SessionContextHolder() {
        this.sessionId = RandomValueUtils.randomUUID();
        long now = ClockUtils.getTime();
        this.sessionStartTime = now;
        this.lastActivityTime = now;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getSessionStartTime() {
        return sessionStartTime;
    }

    public String getSessionIdAndExtendSession() {
        long now = ClockUtils.getTime();
        if (lastActivityTime + Constants.SESSION_DURATION < now) {
            sessionId = RandomValueUtils.randomUUID();
            sessionStartTime = now;
        }
        lastActivityTime = now;
        return sessionId;
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }
}
