package io.xenn.android.context;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import io.xenn.android.utils.ClockUtils;
import io.xenn.android.utils.RandomValueUtils;

import static org.junit.Assert.*;

public class SessionContextHolderTest {

    @Test
    public void it_should_initialize_session_id_and_session_start_time() {
        ClockUtils.freeze();
        RandomValueUtils.freeze();
        long sessionStartTime = ClockUtils.getTime();
        String sessionId = RandomValueUtils.randomUUID();

        SessionContextHolder sessionContextHolder = new SessionContextHolder();
        assertEquals(sessionId, sessionContextHolder.getSessionId());
        assertTrue(sessionStartTime == sessionContextHolder.getSessionStartTime());
        assertTrue(sessionStartTime == sessionContextHolder.getLastActivityTime());
        assertTrue(sessionContextHolder.getExternalParameters().isEmpty());
        assertTrue(sessionContextHolder.getIntentParameters().isEmpty());
        assertEquals(SessionState.SESSION_INITIALIZED, sessionContextHolder.getSessionState());
        ClockUtils.unFreeze();
        RandomValueUtils.unFreeze();

    }

    @Test
    public void it_should_get_session_id_and_extend_session_time() {
        long expectedTime = 1587133370000L;
        ClockUtils.freeze(expectedTime);
        RandomValueUtils.freeze();
        String sessionId = RandomValueUtils.randomUUID();
        SessionContextHolder sessionContextHolder = new SessionContextHolder();
        String resolvedSessionId = sessionContextHolder.getSessionIdAndExtendSession();
        long lastActivityTime = sessionContextHolder.getLastActivityTime();

        assertEquals(sessionId, resolvedSessionId);
        assertEquals(lastActivityTime, expectedTime);
        ClockUtils.unFreeze();
        RandomValueUtils.unFreeze();
    }

    @Test
    public void it_should_get_create_new_session_id_when_session_expired() {
        long expectedTime = 1587133370000L;
        ClockUtils.freeze(expectedTime);
        RandomValueUtils.freeze();
        String sessionId = RandomValueUtils.randomUUID();
        SessionContextHolder sessionContextHolder = new SessionContextHolder();
        String resolvedSessionId = sessionContextHolder.getSessionIdAndExtendSession();
        RandomValueUtils.unFreeze();

        long expectedTimeInFuture = expectedTime + 60 * 60 * 1000L;
        ClockUtils.freeze(expectedTimeInFuture);
        String resolvedSessionId2 = sessionContextHolder.getSessionIdAndExtendSession();
        long lastActivityTime = sessionContextHolder.getLastActivityTime();
        long sessionStartTime = sessionContextHolder.getSessionStartTime();

        assertNotEquals(resolvedSessionId, resolvedSessionId2);
        assertTrue(lastActivityTime == expectedTimeInFuture);
        assertTrue(sessionStartTime == expectedTimeInFuture);
        assertTrue(sessionContextHolder.getExternalParameters().isEmpty());
        assertTrue(sessionContextHolder.getIntentParameters().isEmpty());
        assertEquals(SessionState.SESSION_RESTARTED, sessionContextHolder.getSessionState());
        ClockUtils.unFreeze();
    }

    @Test
    public void it_should_log_in_member() {
        SessionContextHolder sessionContextHolder = new SessionContextHolder();
        String memberId = "memberId";
        sessionContextHolder.login(memberId);
        assertEquals(memberId, sessionContextHolder.getMemberId());
    }

    @Test
    public void it_should_log_out_member() {
        SessionContextHolder sessionContextHolder = new SessionContextHolder();
        String memberId = "memberId";
        sessionContextHolder.login(memberId);
        assertEquals(memberId, sessionContextHolder.getMemberId());
        sessionContextHolder.logout();
        assertNull(sessionContextHolder.getMemberId());
    }

    @Test
    public void it_should_change_session_state_when_session_started() {
        SessionContextHolder sessionContextHolder = new SessionContextHolder();
        sessionContextHolder.startSession();
        assertEquals(SessionState.SESSION_STARTED, sessionContextHolder.getSessionState());
    }

    @Test
    public void it_should_update_external_parameters() {
        Map<String, String> externalParameters = new HashMap<>();
        externalParameters.put("a", "b");
        externalParameters.put("c", "e");
        externalParameters.put("d", "f");
        externalParameters.put("campaignId", "campaignId");
        externalParameters.put("campaignDate", "campaignDate");
        externalParameters.put("pushId", "pushId");
        externalParameters.put("url", "url");
        externalParameters.put("utm_source", "utm_source");
        externalParameters.put("utm_medium", "utm_medium");
        externalParameters.put("utm_campaign", "utm_campaign");
        externalParameters.put("utm_term", "utm_term");
        externalParameters.put("utm_content", "utm_content");

        SessionContextHolder sessionContextHolder = new SessionContextHolder();
        sessionContextHolder.updateExternalParameters(externalParameters);

        Map<String, Object> boundedExternalParameters = sessionContextHolder.getExternalParameters();
        assertEquals("campaignId", boundedExternalParameters.get("campaignId"));
        assertEquals("campaignDate", boundedExternalParameters.get("campaignDate"));
        assertEquals("pushId", boundedExternalParameters.get("pushId"));
        assertEquals("url", boundedExternalParameters.get("url"));
        assertEquals("utm_source", boundedExternalParameters.get("utm_source"));
        assertEquals("utm_medium", boundedExternalParameters.get("utm_medium"));
        assertEquals("utm_campaign", boundedExternalParameters.get("utm_campaign"));
        assertEquals("utm_term", boundedExternalParameters.get("utm_term"));
        assertEquals("utm_content", boundedExternalParameters.get("utm_content"));
        assertFalse(boundedExternalParameters.containsKey("a"));
        assertFalse(boundedExternalParameters.containsKey("b"));
        assertFalse(boundedExternalParameters.containsKey("c"));
        assertFalse(boundedExternalParameters.containsKey("d"));

    }

    @Test
    public void it_should_update_external_parameters_when_parameter_present() {
        Map<String, String> externalParameters = new HashMap<>();
        externalParameters.put("a", "b");
        externalParameters.put("campaignId", "campaignId");
        SessionContextHolder sessionContextHolder = new SessionContextHolder();
        sessionContextHolder.updateExternalParameters(externalParameters);

        Map<String, Object> boundedExternalParameters = sessionContextHolder.getExternalParameters();
        assertEquals("campaignId", boundedExternalParameters.get("campaignId"));
        assertFalse(boundedExternalParameters.containsKey("a"));
        assertFalse(boundedExternalParameters.containsKey("utm_campaign"));
    }
}