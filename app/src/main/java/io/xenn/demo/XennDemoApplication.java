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
                        .init("XENN-XMjJ4RzzvbPc0T2", "https://c.xenn.io:443/")
                        .apiUrl("newApiUrl")
                        .useXennPlugin(FcmKitPlugin.class);
        Xennio.configure(this, xennConfig);
    }
}
