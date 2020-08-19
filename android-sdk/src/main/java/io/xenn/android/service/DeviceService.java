package io.xenn.android.service;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

import io.xenn.android.common.Constants;


public class DeviceService {

    private final Context context;

    public DeviceService(Context context) {
        this.context = context;
    }

    public String getAppVersion() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo pInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (Exception e) {
        }
        return null;
    }

    public String getCarrier() {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getNetworkOperatorName();
    }


    public String getOsVersion() {
        return (Build.VERSION.RELEASE == null) ? Constants.UNKNOWN_PROPERTY_VALUE : Build.VERSION.RELEASE;
    }

    public String getModel() {
        return (Build.MODEL == null) ? Constants.UNKNOWN_PROPERTY_VALUE : Build.MODEL;
    }

    public String getManufacturer() {
        return (Build.MANUFACTURER == null) ? Constants.UNKNOWN_PROPERTY_VALUE : Build.MANUFACTURER;
    }

    public String getBrand() {
        return (Build.BRAND == null) ? Constants.UNKNOWN_PROPERTY_VALUE : Build.BRAND;
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public Uri getSound(String sound) {
        int id = context.getResources().getIdentifier(sound, "raw", context.getPackageName());
        if (id != 0) {
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + id);
        } else {
            return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
    }
}
