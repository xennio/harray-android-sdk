package io.xenn.hmskit;

import android.content.Context;

import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;

public class Main {

    public void g(final Context context) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String appId = AGConnectServicesConfig.fromContext(context)
                            .getString("client/app_id");
                    String token = HmsInstanceId.getInstance(context)
                            .getToken(appId, "HCM");
//                    Xennio.notifications().savePushToken(token);
                } catch (ApiException e) {
//                    XennioLogger.log("Receiving Token Failed: " + e.getMessage());
                }
            }
        }.start();
    }
}
