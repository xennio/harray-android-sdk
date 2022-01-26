package io.xenn.android.event.inappnotification;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.xenn.android.model.inappnotification.InAppNotificationResponse;
import io.xenn.android.utils.XennioLogger;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class InAppNotificationViewManager {

    private static final int POPUP_WINDOW_PADDING = 16;
    private final int horizontalWindowMargin;

    private final Activity activity;
    private final InAppNotificationResponse inAppNotificationResponse;
    private final LinkClickHandler linkClickHandler;
    private final Runnable showHandler;
    private final Runnable closeHandler;

    private WebView webView;
    private PopupWindow popupWindow;

    public InAppNotificationViewManager(
            Activity activity,
            InAppNotificationResponse inAppNotificationResponse,
            LinkClickHandler linkClickHandler,
            Runnable showHandler,
            Runnable closeHandler) {
        this.activity = activity;
        this.inAppNotificationResponse = inAppNotificationResponse;
        this.linkClickHandler = linkClickHandler;
        this.showHandler = showHandler;
        this.closeHandler = closeHandler;
        this.horizontalWindowMargin = dpToPx(60);
    }

    public void show() {
        final String htmlBase64Str = getBase64Str(inAppNotificationResponse);
        if (htmlBase64Str == null) {
            XennioLogger.log("No base64 encoded value to be shown as in-app notification.");
            return;
        }
        webView = new WebView(activity);
        webView.clearCache(true);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        webView.addJavascriptInterface(new JavaScriptInterface(this), JavaScriptInterface.JS_OBJ_NAME);
        webView.loadData(htmlBase64Str, "text/html; charset=utf-8", "base64");

        popupWindow = new PopupWindow(webView, getWindowMaxSizeX(activity), 1);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(
                activity.getWindow().getDecorView().getRootView(),
                Gravity.CENTER,
                0,
                0);
        showHandler.run();
    }

    public void dismiss() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                webView.destroy();
                popupWindow.dismiss();
                closeHandler.run();
            }
        });
    }

    public void adjustHeight() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    popupWindow.dismiss();
                    webView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
                    popupWindow.setHeight(dpToPx(webView.getContentHeight() + POPUP_WINDOW_PADDING));
                    popupWindow.showAtLocation(
                            activity.getWindow().getDecorView().getRootView(),
                            Gravity.CENTER,
                            0,
                            0);
                }catch(Exception e){
                    XennioLogger.log("Adjust Height", e);
                }
            }
        }, TimeUnit.SECONDS.toMillis(1L));
    }

    public void triggerUserDefinedLinkClickHandler(String link) {
        if (linkClickHandler != null) {
            XennioLogger.log("User defined link click handler trigger for link:" + link);
            linkClickHandler.handle(link);
        } else {
            XennioLogger.log("No user defined link click handler defined for link:" + link);
        }
    }

    private String getBase64Str(InAppNotificationResponse inAppNotificationResponse) {
        try {
            return Base64.encodeToString(
                    inAppNotificationResponse.getHtml().getBytes("UTF-8"),
                    Base64.NO_WRAP
            );
        } catch (Exception e) {
            XennioLogger.log("In-app notification base64 encoding error occurred.", e);
            return null;
        }
    }

    private int getWindowMaxSizeX(Activity activity) {
        return getDisplaySizePoint(activity).x - horizontalWindowMargin;
    }

    private Point getDisplaySizePoint(@NonNull Activity activity) {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return point;
    }

    private static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}