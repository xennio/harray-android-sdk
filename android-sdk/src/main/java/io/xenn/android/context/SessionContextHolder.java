package io.xenn.android.context;


import io.xenn.android.common.Constants;
import io.xenn.android.utils.ClockUtils;
import io.xenn.android.utils.RandomValueUtils;

public class SessionContextHolder {

    private String sessionId;
    private String memberId;
    private long sessionStartTime;
    private long lastActivityTime;


    public SessionContextHolder() {
        long now = ClockUtils.getTime();
        this.sessionId = RandomValueUtils.randomUUID();
        this.sessionStartTime = now;
        this.lastActivityTime = now;
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

    protected String getSessionId() {
        return sessionId;
    }

    public Long getSessionStartTime() {
        return sessionStartTime;
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }

    public String getMemberId() {
        return memberId;
    }

    public void login(String memberId) {
        this.memberId = memberId;
    }

    public void logout() {
        this.memberId = null;
    }
}
