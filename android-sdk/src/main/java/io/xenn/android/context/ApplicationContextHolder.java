package io.xenn.android.context;

import android.content.SharedPreferences;

import io.xenn.android.common.Constants;
import io.xenn.android.utils.RandomValueUtils;

public class ApplicationContextHolder {

    private String persistentId;

    public ApplicationContextHolder(SharedPreferences sharedPreferences) {
        String value = sharedPreferences.getString(Constants.SDK_PERSISTENT_ID_KEY, null);
        if (value == null) {
            value = RandomValueUtils.randomUUID();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.SDK_PERSISTENT_ID_KEY, value);
            editor.apply();
        }
        this.persistentId = value;
    }

    public String getPersistentId() {
        return persistentId;
    }
}
