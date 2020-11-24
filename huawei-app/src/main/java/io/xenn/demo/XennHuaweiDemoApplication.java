package io.xenn.demo;

import android.app.Application;

import io.xenn.android.Xennio;
import io.xenn.hmskit.HmsKitPlugin;

public class XennHuaweiDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Xennio.configure(this, "XENN-XMjJ4RzzvbPc0T2", HmsKitPlugin.class);
    }
}
