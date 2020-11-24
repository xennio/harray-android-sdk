package io.xenn.android.context;

import android.content.SharedPreferences;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.TimeZone;

import io.xenn.android.common.Constants;
import io.xenn.android.utils.RandomValueUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationContextHolderTest {

    @Mock
    private SharedPreferences sharedPreferences;
    @Mock
    private SharedPreferences.Editor mockedEditor;

    @Test
    public void it_should_initialize_persistent_id_when_persistent_id_is_not_present_in_shared_preferences() {
        RandomValueUtils.freeze();
        String randomUUID = RandomValueUtils.randomUUID();
        when(sharedPreferences.getString(Constants.SDK_PERSISTENT_ID_KEY, null)).thenReturn(null);
        when(sharedPreferences.edit()).thenReturn(mockedEditor);
        ApplicationContextHolder applicationContextHolder = new ApplicationContextHolder(sharedPreferences);
        assertEquals(randomUUID, applicationContextHolder.getPersistentId());
        assertTrue(applicationContextHolder.isNewInstallation());
        verify(mockedEditor).putString(Constants.SDK_PERSISTENT_ID_KEY, randomUUID);
        verify(mockedEditor).apply();

        RandomValueUtils.unFreeze();
    }

    @Test
    public void it_should_return_persistent_id_when_persistent_id_is_present_in_shared_preferences() {
        String value = "stored-persistent-id";
        when(sharedPreferences.getString(Constants.SDK_PERSISTENT_ID_KEY, null)).thenReturn(value);
        ApplicationContextHolder applicationContextHolder = new ApplicationContextHolder(sharedPreferences);
        assertEquals(applicationContextHolder.getPersistentId(), value);
        verifyNoInteractions(mockedEditor);
    }

    @Test
    public void it_should_return_time_zone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        String value = "stored-persistent-id";
        when(sharedPreferences.getString(Constants.SDK_PERSISTENT_ID_KEY, null)).thenReturn(value);
        ApplicationContextHolder applicationContextHolder = new ApplicationContextHolder(sharedPreferences);
        assertEquals("0", applicationContextHolder.getTimezone());
    }

    @Test
    public void it_should_set_installation_completed_when_invoked() {
        String value = "stored-persistent-id";
        when(sharedPreferences.getString(Constants.SDK_PERSISTENT_ID_KEY, null)).thenReturn(value);
        ApplicationContextHolder applicationContextHolder = new ApplicationContextHolder(sharedPreferences);
        applicationContextHolder.setInstallationCompleted();
        assertFalse(applicationContextHolder.isNewInstallation());
    }
}