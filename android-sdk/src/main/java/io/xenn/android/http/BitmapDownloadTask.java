package io.xenn.android.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.common.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import io.xenn.android.utils.XennioLogger;

public class BitmapDownloadTask {
    private final String urlString;

    public BitmapDownloadTask(String urlString) {
        this.urlString = urlString;
    }

    public Bitmap getBitmap() {
        InputStream inputStream = null;
        try {
            inputStream = new URL(urlString).openConnection().getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            XennioLogger.log("Bitmap download failed " + e.getMessage());
            return null;
        } finally {
            closeStream(inputStream);
        }
    }

    protected String getUrlString() {
        return urlString;
    }

    private void closeStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                XennioLogger.log("Bitmap input stream closing error " + e.getMessage());
            }
        }
    }
}
