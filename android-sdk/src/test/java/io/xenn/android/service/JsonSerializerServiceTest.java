package io.xenn.android.service;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonSerializerServiceTest {


    @Test
    public void it_should_convert_object_to_json_string() {

        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> innerObject = new HashMap<>();
        innerObject.put("x", "inner Value");
        payload.put("a", "string Value");
        payload.put("b", 10);
        payload.put("c", innerObject);

        JsonSerializerService jsonSerializerService = new JsonSerializerService();
        String result = jsonSerializerService.serialize(payload);

        assertEquals(result, "{\"a\":\"string Value\",\"b\":10,\"c\":{\"x\":\"inner Value\"}}");
    }


}
