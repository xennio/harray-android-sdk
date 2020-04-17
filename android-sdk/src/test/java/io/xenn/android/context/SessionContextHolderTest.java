package io.xenn.android.context;

import org.junit.Test;

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
        ClockUtils.unFreeze();
    }

}