package io.xenn.android;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.xenn.android.common.Constants;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class XennioTest {

    @Mock
    private Context context;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Mock
    private SharedPreferences.Editor mockEditor;

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

}