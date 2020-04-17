package io.xenn.android;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class XennioTest {

    @Mock
    private Context context;

    @Test
    public void it_should_return_same_instance_when_get_method_called_more_than_one_time() {
        Xennio.configure(context, "SdkKey");
        Xennio xennio = Xennio.getInstance();
        Xennio xennio2 = Xennio.getInstance();
        assertEquals(xennio, xennio2);
    }

    @Test(expected = IllegalStateException.class)
    public void it_should_throw_illegal_state_exception_when_configuration_is_not_made() {
        Xennio.getInstance();
    }

}