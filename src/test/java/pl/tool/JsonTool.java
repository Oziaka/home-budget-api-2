package pl.tool;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTool {
    public static String parseJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
