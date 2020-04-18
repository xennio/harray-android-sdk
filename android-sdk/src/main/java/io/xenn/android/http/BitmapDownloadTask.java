package io.xenn.android.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URL;

import io.xenn.android.utils.XennioLogger;

public class BitmapDownloadTask {
    private final String urlString;

    public BitmapDownloadTask(String urlString) {
        this.urlString = urlString;
    }

    public Bitmap getBitmap() {
        try {
            return BitmapFactory.decodeStream(new URL(urlString).openConnection().getInputStream());
        } catch (Exception e) {
            XennioLogger.log("Bitmap download failed " + e.getMessage());
            return null;
        }
    }

    protected String getUrlString() {
        return urlString;
    }
}
