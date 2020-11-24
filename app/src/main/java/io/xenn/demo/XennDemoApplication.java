package io.xenn.demo;

import android.app.Application;

import io.xenn.android.Xennio;
import io.xenn.fcmkit.FcmKitPlugin;

public class XennDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Xennio.configure(this, "XENN-XMjJ4RzzvbPc0T2", FcmKitPlugin.class);
    }
}
