package pl.tool;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTool {


   public static String asJsonString (Object obj) {
      try {
         return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}
