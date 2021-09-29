package io.xenn.android.event;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.xenn.android.common.ResponseBodyHandler;
import io.xenn.android.common.ResultConsumer;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.service.HttpService;
import io.xenn.android.service.JsonDeserializerService;

public class PushMessagesHistoryProcessorHandler {

    private final SessionContextHolder sessionContextHolder;
    private final HttpService httpService;
    private final String sdkKey;
    private final JsonDeserializerService jsonDeserializerService;

    public PushMessagesHistoryProcessorHandler(
            SessionContextHolder sessionContextHolder,
            HttpService httpService,
            String sdkKey,
            JsonDeserializerService jsonDeserializerService) {
        this.sessionContextHolder = sessionContextHolder;
        this.httpService = httpService;
        this.sdkKey = sdkKey;
        this.jsonDeserializerService = jsonDeserializerService;
    }

    public void getPushMessagesHistory(int size,
                                       @NonNull ResultConsumer<List<Map<String, String>>> callback) {
        if (sessionContextHolder.getMemberId() == null) {
            throw new IllegalArgumentException("memberId cannot be null for push messages history. " +
                    "Use Xennio.login(memberId) method first.");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("sdkKey", sdkKey);
        params.put("memberId", sessionContextHolder.getMemberId());
        params.put("size", String.valueOf(size));
        httpService.getApiRequest("/push-messages-history", params, new ResponseBodyHandler<List<Map<String, String>>>() {
            @Override
            public List<Map<String, String>> handle(String rawResponseBody) {
                return jsonDeserializerService.deserializeToMapList(rawResponseBody);
            }
        }, callback);
    }
}
