package io.xenn.android.service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.telephony.TelephonyManager;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static android.os.Build.VERSION_CODES.P;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {P}, qualifiers = "fr-rFR-w360dp-h640dp-xhdpi")
public class DeviceServiceTest {

    DeviceService deviceService = new DeviceService(RuntimeEnvironment.systemContext);


    @Test
    public void it_should_return_build_model() {
        assertEquals("robolectric", deviceService.getModel());
    }

    @Test
    public void it_should_return_os_version() {
        assertEquals("9", deviceService.getOsVersion());
    }

    @Test
    public void it_should_get_manufacturer() {
        assertEquals("4.1.2", deviceService.getManufacturer());
    }

    @Test
    public void it_should_get_brand() {
        assertEquals("Android", deviceService.getBrand());
    }

    @Test
    public void it_should_get_default_sound_uri_when_sound_is_not_present() {
        Uri result = deviceService.getSound("soundnoexists");
        assertEquals(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), result);
    }

    @Ignore
    public void it_should_get_app_version() throws PackageManager.NameNotFoundException {
        Context mockContext = mock(Context.class);
        DeviceService fakeDeviceService = new DeviceService(mockContext);
        PackageManager packageManager = mock(PackageManager.class);
        PackageInfo pInfo = mock(PackageInfo.class);

        when(mockContext.getPackageManager()).thenReturn(packageManager);
        when(packageManager.getPackageInfo(anyString(), eq(0))).thenReturn(pInfo);

        assertEquals("ApplicationVersion", fakeDeviceService.getAppVersion());
    }

    @Test
    public void it_should_get_carrier() {
        Context mockContext = mock(Context.class);
        DeviceService fakeDeviceService = new DeviceService(mockContext);
        TelephonyManager mockTelephonyManager = mock(TelephonyManager.class);
        when(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).thenReturn(mockTelephonyManager);
        when(mockTelephonyManager.getNetworkOperator()).thenReturn("AT");
        assertEquals("AT", fakeDeviceService.getCarrier());
    }

}