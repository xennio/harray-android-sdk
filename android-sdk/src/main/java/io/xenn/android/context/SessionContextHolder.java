package io.xenn.android.context;

import java.util.HashMap;
import java.util.Map;

import io.xenn.android.common.Constants;
import io.xenn.android.utils.ClockUtils;
import io.xenn.android.utils.RandomValueUtils;

public class SessionContextHolder {

    private String sessionId;
    private String memberId;
    private long sessionStartTime;
    private long lastActivityTime;
    private Map<String, Object> externalParameters = new HashMap<>();
    private SessionState sessionState = SessionState.SESSION_INITIALIZED;

    public SessionContextHolder() {
        long now = ClockUtils.getTime();
        this.sessionId = RandomValueUtils.randomUUID();
        this.sessionStartTime = now;
        this.lastActivityTime = now;
    }

    public String getSessionIdAndExtendSession() {
        long now = ClockUtils.getTime();
        if (lastActivityTime + Constants.SESSION_DURATION < now) {
            restartSession();
        }
        lastActivityTime = now;
        return sessionId;
    }

    public void restartSession() {
        long now = ClockUtils.getTime();
        sessionId = RandomValueUtils.randomUUID();
        sessionStartTime = now;
        sessionState = SessionState.SESSION_RESTARTED;
        externalParameters = new HashMap<>();
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

    public Map<String, Object> getExternalParameters() {
        return externalParameters;
    }


    public void updateExternalParameters(Map<String, Object> data) {
        for (String eachKey : Constants.EXTERNAL_PARAMETER_KEYS) {
            if (data.containsKey(eachKey)) {
                externalParameters.put(eachKey, data.get(eachKey));
            }
        }
    }

    public SessionState getSessionState() {
        return sessionState;
    }

    public void startSession() {
        sessionState = SessionState.SESSION_STARTED;
    }

}
