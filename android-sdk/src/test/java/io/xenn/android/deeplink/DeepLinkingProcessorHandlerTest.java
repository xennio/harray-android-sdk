package io.xenn.android.deeplink;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

import io.xenn.android.context.SessionContextHolder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeepLinkingProcessorHandlerTest {

    @InjectMocks
    private DeepLinkingProcessorHandler deepLinkingProcessorHandler;

    @Mock
    private SessionContextHolder sessionContextHolder;

    @Test
    public void it_should_return_false_when_key_does_not_exists() {
        when(sessionContextHolder.getIntentParameters()).thenReturn(new HashMap<String, String>());
        assertFalse(deepLinkingProcessorHandler.hasKey("foo"));
    }

    @Test
    public void it_should_get_value_of_given_key_when_exists() {
        HashMap<String, String> intentParameters = new HashMap<>();
        intentParameters.put("foo", "bar");
        when(sessionContextHolder.getIntentParameters()).thenReturn(intentParameters);
        assertEquals("bar", deepLinkingProcessorHandler.get("foo"));
    }


}