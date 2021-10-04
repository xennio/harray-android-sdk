package io.xenn.demo;

import android.app.Application;

import io.xenn.android.Xennio;
import io.xenn.android.common.XennConfig;
import io.xenn.android.event.inappnotification.LinkClickHandler;
import io.xenn.fcmkit.FcmKitPlugin;

public class XennDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XennConfig xennConfig =
                XennConfig
                        .init("XENN-XMjJ4RzzvbPc0T2")
                        .useXennPlugin(FcmKitPlugin.class).inAppNotificationLinkClickHandler(
                        new LinkClickHandler() {
                            @Override
                            public void handle(String link) {

                            }
                        }
                );
        Xennio.configure(this, xennConfig);
    }
}
