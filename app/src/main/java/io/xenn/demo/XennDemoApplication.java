package io.xenn.demo;

import android.app.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.xenn.android.Xennio;
import io.xenn.android.context.XennPlugin;
import io.xenn.hmskit.HmsKitPlugin;

public class XennDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Xennio.configure(this, "XENN-XMjJ4RzzvbPc0T2", HmsKitPlugin.class);
    }
}
