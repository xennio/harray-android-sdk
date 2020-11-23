package io.xenn.demo;

import android.app.Application;

import java.util.Arrays;

import io.xenn.android.Xennio;
import io.xenn.hmskit.HmsKitPlugin;

public class XennDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Xennio.configure(this, "XENN-XMjJ4RzzvbPc0T2", Arrays.asList(new HmsKitPlugin()));

    }
}
