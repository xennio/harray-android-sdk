package io.xenn.android;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import io.xenn.android.common.Constants;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.context.SessionState;
import io.xenn.android.context.XennPlugin;
import io.xenn.android.event.EventProcessorHandler;
import io.xenn.android.event.SDKEventProcessorHandler;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class XennioTest {

    @Mock
    private Context context;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Mock
    private SharedPreferences.Editor mockEditor;

    @Mock
    private TestXennPlugin testXennPlugin;

    @Test
    public void it_should_return_same_instance_when_get_method_called_more_than_one_time() {
        when(context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        Xennio.configure(context, "SdkKey");
        Xennio xennio = Xennio.getInstance();
        Xennio xennio2 = Xennio.getInstance();
        assertEquals(xennio, xennio2);
    }

    @Test(expected = IllegalStateException.class)
    public void it_should_throw_illegal_state_exception_when_configuration_is_not_made() {
        Xennio.getInstance();
    }

    @Test
    public void it_should_initialized_shared_prefs_with_xenn_key() {
        when(context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);

        Xennio.configure(context, "SdkKey");

        verify(mockSharedPreferences).getString(Constants.SDK_PERSISTENT_ID_KEY, null);
        verify(mockEditor).putString(eq(Constants.SDK_PERSISTENT_ID_KEY), anyString());
        verify(mockEditor).apply();
    }

    @Test
    public void it_should_set_member_id_to_context_when_member_id_is_not_empty() {
        when(context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);

        Xennio.configure(context, "SdkKey");
        Xennio.login("memberId");

        Xennio instance = Xennio.getInstance();

        assertEquals("memberId", instance.sessionContextHolder.getMemberId());
    }

    @Test
    public void it_should_set_member_id_to_context_when_member_id_is_not_empty_and_trigger_onLogin_method_of_plugins() {
        when(context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);

        Xennio.configure(context, "SdkKey", singletonList(testXennPlugin));

        Xennio instance = Xennio.getInstance();

        Xennio.login("memberId");

        assertEquals("memberId", instance.sessionContextHolder.getMemberId());
        verify(testXennPlugin).onLogin();
    }

    @Test
    public void it_should_not_set_member_id_to_context_when_member_id_is_empty() {
        when(context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);

        Xennio.configure(context, "SdkKey");
        Xennio.login("");

        Xennio instance = Xennio.getInstance();

        assertNull(instance.sessionContextHolder.getMemberId());
    }

    @Test
    public void it_should_set_null_as_member_id_when_log_out_invoked() {
        when(context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);

        Xennio.configure(context, "SdkKey", singletonList(testXennPlugin));
        Xennio.login("memberId");
        Xennio instance = Xennio.getInstance();
        assertEquals("memberId", instance.sessionContextHolder.getMemberId());

        Xennio.logout();

        assertNull(instance.sessionContextHolder.getMemberId());
        verify(testXennPlugin).onLogout();
    }

    @Test
    public void it_should_call_session_start_and_new_installation_when_eventing_called() {
        when(context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        Xennio.configure(context, "SdkKey");
        Xennio instance = Xennio.getInstance();
        instance.applicationContextHolder = mock(ApplicationContextHolder.class);
        instance.sdkEventProcessorHandler = mock(SDKEventProcessorHandler.class);
        instance.sessionContextHolder = mock(SessionContextHolder.class);

        when(instance.sessionContextHolder.getSessionState()).thenReturn(SessionState.SESSION_INITIALIZED);
        when(instance.applicationContextHolder.isNewInstallation()).thenReturn(true);
        Xennio.eventing();

        verify(instance.sessionContextHolder).startSession();
        verify(instance.sdkEventProcessorHandler).newInstallation();
        verify(instance.applicationContextHolder).setInstallationCompleted();

    }

    @Test
    public void it_should_not_call_session_start_and_new_installation_when_eventing_called_second_time() {
        when(context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        Xennio.configure(context, "SdkKey");
        Xennio instance = Xennio.getInstance();
        instance.applicationContextHolder = mock(ApplicationContextHolder.class);
        instance.sdkEventProcessorHandler = mock(SDKEventProcessorHandler.class);
        instance.sessionContextHolder = mock(SessionContextHolder.class);

        when(instance.sessionContextHolder.getSessionState()).thenReturn(SessionState.SESSION_STARTED);
        EventProcessorHandler handler = Xennio.eventing();

        verify(instance.sessionContextHolder, never()).startSession();
        verifyNoInteractions(instance.sdkEventProcessorHandler);
        verifyNoInteractions(instance.applicationContextHolder);
        assertEquals(handler, instance.eventProcessorHandler);

    }

    @Test
    public void it_should_synchronize_intent_data() {
        when(context.getSharedPreferences(Constants.PREF_COLLECTION_NAME, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        Xennio.configure(context, "SdkKey");
        Xennio instance = Xennio.getInstance();
        instance.sessionContextHolder = mock(SessionContextHolder.class);
        Map<String, Object> intent = new HashMap<>();
        Xennio.synchronizeIntentData(intent);

        verify(instance.sessionContextHolder).updateExternalParameters(intent);
    }

}

class TestXennPlugin extends XennPlugin {

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
    }

    @Override
    public void onLogin() {
        super.onLogin();
    }

    @Override
    public void onLogout() {
        super.onLogout();
    }
}