package pl.tool;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

   private static ObjectMapper mapper = new ObjectMapper();

   public static String parseJson(Object obj) {
      try {
         return new ObjectMapper().writeValueAsString(obj);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public static <T> T parseObject(String json, T type) {
      try {
         return (T) mapper.readValue(json, type.getClass());
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}
