package pl.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.parser.JSONParser;

import java.util.List;

public class JsonTool {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String parseJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object parseObject(String json, Class clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Object> parseObjects(String json, Class clazz) {
        try {
            return mapper.readValue(json, new TypeReference<List<Object>>() {
            })
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
