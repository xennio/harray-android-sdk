package io.xenn.demo;

import android.app.Application;

import io.xenn.android.XennioAPI;

public class XennDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XennioAPI.init(this, "XENN-XMjJ4RzzvbPc0T2");

    }
}
