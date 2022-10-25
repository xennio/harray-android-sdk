package io.xenn.android.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.xenn.android.common.ResponseBodyHandler;
import io.xenn.android.common.ResultConsumer;
import io.xenn.android.context.ApplicationContextHolder;
import io.xenn.android.context.SessionContextHolder;
import io.xenn.android.service.EncodingService;
import io.xenn.android.service.HttpService;
import io.xenn.android.service.JsonDeserializerService;
import io.xenn.android.utils.XennioLogger;

public class RecommendationProcessorHandler {

    private final ApplicationContextHolder applicationContextHolder;
    private final SessionContextHolder sessionContextHolder;
    private final HttpService httpService;
    private final String sdkKey;
    private final JsonDeserializerService jsonDeserializerService;
    private final EncodingService encodingService;

    public RecommendationProcessorHandler(
            ApplicationContextHolder applicationContextHolder,
            SessionContextHolder sessionContextHolder,
            HttpService httpService,
            String sdkKey,
            JsonDeserializerService jsonDeserializerService,
            EncodingService encodingService) {
        this.applicationContextHolder = applicationContextHolder;
        this.sessionContextHolder = sessionContextHolder;
        this.httpService = httpService;
        this.sdkKey = sdkKey;
        this.jsonDeserializerService = jsonDeserializerService;
        this.encodingService = encodingService;
    }

    public void getRecommendations(@NonNull String boxId,
                                   @Nullable String entityId,
                                   int size,
                                   @NonNull ResultConsumer<List<Map<String, String>>> callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("sdkKey", sdkKey);
        params.put("pid", applicationContextHolder.getPersistentId());
        params.put("boxId", boxId);
        if (sessionContextHolder.getMemberId() != null) {
            params.put("memberId", sessionContextHolder.getMemberId());
        }
        if (entityId != null) {
            params.put("entityId", entityId);
        }
        params.put("size", String.valueOf(size));
        httpService.getApiRequest("/recommendation", params, new ResponseBodyHandler<List<Map<String, String>>>() {
            @Override
            public List<Map<String, String>> handle(String rawResponseBody) {
                return jsonDeserializerService.deserializeToMapList(rawResponseBody);
            }
        }, callback);
    }

    public void getRecommendations(@NonNull String boxId,
                                   int size,
                                   @Nullable String filterExpression,
                                   @Nullable String sortingFactors,
                                   @NonNull ResultConsumer<List<Map<String, String>>> callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("sdkKey", sdkKey);
        params.put("pid", applicationContextHolder.getPersistentId());
        params.put("boxId", boxId);
        if (sessionContextHolder.getMemberId() != null) {
            params.put("memberId", sessionContextHolder.getMemberId());
        }
        if (filterExpression != null) {
            try {
                params.put("filterExpression", encodingService.getUrlEncodedString(filterExpression));
                if (sortingFactors != null) {
                    params.put("sortExpression", sortingFactors);
                }
                params.put("size", String.valueOf(size));
                httpService.getApiRequest("/recommendation", params, new ResponseBodyHandler<List<Map<String, String>>>() {
                    @Override
                    public List<Map<String, String>> handle(String rawResponseBody) {
                        return jsonDeserializerService.deserializeToMapList(rawResponseBody);
                    }
                }, callback);
            } catch (UnsupportedEncodingException e) {
                XennioLogger.log("filterExpression Encoding error", e);
            }
        }
    }
}
