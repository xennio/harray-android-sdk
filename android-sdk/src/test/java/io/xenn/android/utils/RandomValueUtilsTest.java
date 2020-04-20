package io.xenn.android.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RandomValueUtilsTest {

    @Test
    public void it_should_generate_random_value_if_it_is_not_frozen() {
        String random1 = RandomValueUtils.randomUUID();
        String random2 = RandomValueUtils.randomUUID();

        assertNotEquals(random1, random2);
    }

    @Test
    public void it_should_not_generate_new_value_if_it_is_frozen() {
        RandomValueUtils.freeze();
        String random1 = RandomValueUtils.randomUUID();
        String random2 = RandomValueUtils.randomUUID();
        assertEquals(random1, random2);
        RandomValueUtils.unFreeze();
    }

}