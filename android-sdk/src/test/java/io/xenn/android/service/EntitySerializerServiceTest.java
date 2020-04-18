package io.xenn.android.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EntitySerializerServiceTest {

    @InjectMocks
    private EntitySerializerService entitySerializerService;

    @Mock
    private JsonSerializerService jsonSerializerService;

    @Mock
    private EncodingService encodingService;

    @Test
    public void it_should_convert_entity_to_base_64_url_encoded_string() throws UnsupportedEncodingException {
        Map<String, Object> event = new HashMap<>();

        when(jsonSerializerService.serialize(event)).thenReturn("JSONVALUE");
        when(encodingService.getUrlEncodedString("JSONVALUE")).thenReturn("URLENCODEDVALUE");
        when(encodingService.getBase64EncodedString("URLENCODEDVALUE")).thenReturn("BASE64STRINGVALUE");

        String serializedEntity = entitySerializerService.serialize(event);
        assertEquals("BASE64STRINGVALUE", serializedEntity);
    }
}