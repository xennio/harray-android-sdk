package io.xenn.android.utils;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class IOUtils {

    public static void close(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                XennioLogger.log("Reader closing error: " + e.getMessage());
            }
        }
    }

    public static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                XennioLogger.log("Input stream closing error: " + e.getMessage());
            }
        }
    }

    public static String readAll(InputStream in) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            XennioLogger.log("Read operation error occurred from InputStream:" + e.getMessage());
            return null;
        } finally {
            close(bufferedReader);
        }
    }
}
