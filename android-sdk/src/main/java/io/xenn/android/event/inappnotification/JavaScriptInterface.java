package io.xenn.android.event.inappnotification;

import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import io.xenn.android.utils.XennioLogger;

public class JavaScriptInterface {

    static final String JS_OBJ_NAME = "XennAndroid";
    private static final String EVENT_TYPE_KEY = "eventType";
    private static final String CLOSE_POPUP_ACTION = "close";
    private static final String RENDER_COMPLETED_ACTION = "renderCompleted";

    private final InAppNotificationViewManager viewManager;

    public JavaScriptInterface(InAppNotificationViewManager viewManager) {
        this.viewManager = viewManager;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        XennioLogger.log("Message from JS: " + message);
        try {
            JSONObject jsonObject = new JSONObject(message);
            String eventType = jsonObject.getString(EVENT_TYPE_KEY);
            if (CLOSE_POPUP_ACTION.equals(eventType)) {
                viewManager.dismiss();
            } else if (RENDER_COMPLETED_ACTION.equals(eventType)) {
                viewManager.adjustHeight();
            } else {
                XennioLogger.log("Unhandled JS message: " + message);
            }
        } catch (JSONException e) {
            XennioLogger.log("JS message processing error", e);
        }
    }

    @JavascriptInterface
    public void log(String message) {
        XennioLogger.log("JS message arrived: " + message);
    }
}