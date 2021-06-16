package io.xenn.demo;

import android.app.Application;

import io.xenn.android.Xennio;
import io.xenn.android.common.XennConfig;
import io.xenn.fcmkit.FcmKitPlugin;

public class XennDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XennConfig xennConfig =
                XennConfig
                        .init("XENN-c9s9cGMWHc186Ei")
                        .useXennPlugin(FcmKitPlugin.class);
        Xennio.configure(this, xennConfig);
    }
}
