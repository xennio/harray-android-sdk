package io.xenn.android.service;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonDeserializerServiceTest {

    private JsonDeserializerService service = new JsonDeserializerService();

    @Test
    public void it_should_deserialize_json_to_map_list() {

        List<Map<String, String>> result = service.deserializeToMapList(
                "[ { \"id\": \"id1\",  \"name\": \"name1\"  }, {  \"id\": \"id2\",  \"name\": \"name2\"  } ]"
        );

        assertEquals(result.size(), 2);
        assertEquals(result.get(0).get("id"), "id1");
        assertEquals(result.get(0).get("name"), "name1");
        assertEquals(result.get(1).get("id"), "id2");
        assertEquals(result.get(1).get("name"), "name2");
    }

    @Test
    public void it_should_deserialize_json_to_map() {
        Map<String, String> result = service.deserializeToMap(
                "{ \"id\": \"id1\",  \"name\": \"name1\", \"nullField\": \"null\"  }"
        );

        assertEquals(result.get("id"), "id1");
        assertEquals(result.get("name"), "name1");
        assertNull(result.get("nullField"));
    }
}