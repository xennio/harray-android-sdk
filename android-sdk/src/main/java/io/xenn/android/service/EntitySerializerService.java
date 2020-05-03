package io.xenn.android.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class EntitySerializerService {

    private final EncodingService encodingService;
    private final JsonSerializerService jsonSerializerService;

    public EntitySerializerService(EncodingService encodingService, JsonSerializerService jsonSerializerService) {
        this.encodingService = encodingService;
        this.jsonSerializerService = jsonSerializerService;
    }

    public String serializeToBase64(Map<String, Object> event) throws UnsupportedEncodingException {
        String jsonValue = jsonSerializerService.serialize(event);
        String urlEncodedString = encodingService.getUrlEncodedString(jsonValue);
        String base64EncodedString = encodingService.getBase64EncodedString(urlEncodedString);
        return base64EncodedString;
    }

    public String serializeToJson(Map<String, Object> event) {
        return jsonSerializerService.serialize(event);
    }
}
