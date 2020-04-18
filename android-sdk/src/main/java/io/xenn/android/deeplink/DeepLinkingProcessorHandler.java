package io.xenn.android.deeplink;

import io.xenn.android.context.SessionContextHolder;

public class DeepLinkingProcessorHandler {
    private final SessionContextHolder sessionContextHolder;

    public DeepLinkingProcessorHandler(SessionContextHolder sessionContextHolder) {
        this.sessionContextHolder = sessionContextHolder;
    }

    public boolean hasKey(String key) {
        return sessionContextHolder.getIntentParameters().containsKey(key);
    }

    public String get(String key) {
        return sessionContextHolder.getIntentParameters().get(key);
    }
}
