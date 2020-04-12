package io.xenn.android.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URL;

public class ImageDownloadManager {

    private static final int connectionTimeout = 15000;
    private static final int readTimeout = 10000;

    private static ImageDownloadManager instance;

    public static ImageDownloadManager getInstance() {
        return instance;
    }

    static {
        instance = new ImageDownloadManager();
    }

    private ImageDownloadManager() {
    }

    public Bitmap getBitmap(final String urlString) {
        try {
            return BitmapFactory.decodeStream(new URL(urlString).openConnection().getInputStream());
        } catch (Exception e) {
            XennioLogger.log(e.getMessage());
            return null;
        }
    }

}
