package io.xenn.android.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClockUtilsTest {

    @Test
    public void it_should_get_current_time_if_it_is_not_frozen() throws InterruptedException {
        long time1 = ClockUtils.getTime();
        Thread.sleep(1000);
        long time2 = ClockUtils.getTime();
        assertFalse(time1 == time2);
    }

    @Test
    public void it_should_get_same_timestamp_if_it_is_frozen() throws InterruptedException {
        ClockUtils.freeze();
        long time1 = ClockUtils.getTime();
        Thread.sleep(1000);
        long time2 = ClockUtils.getTime();
        assertTrue(time1 == time2);
        ClockUtils.unFreeze();
    }

    @Test
    public void it_should_get_given_timestamp_if_it_is_frozen() throws InterruptedException {
        long expectedTime = 1587133370000L;
        ClockUtils.freeze(expectedTime);
        long time1 = ClockUtils.getTime();
        long time2 = ClockUtils.getTime();
        assertTrue(time1 == time2);
        assertEquals(expectedTime, time1);
        ClockUtils.unFreeze();
    }
}