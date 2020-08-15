package pl.tool;

import pl.user.UserDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.List;
import java.util.stream.Collectors;

public class PrincipalInterpolator {

   private static final String PRINCIPAL_SCHEMA_PATH = TestFile.FILE_PATH + "\\PrincipalSchema.JSON";

   public String interpolate(UserDto userDto) {
      return new Formatter().format(getPrincipalSchema(), userDto.getPassword(), userDto.getEmail(), userDto.getPassword(), userDto.getEmail()).toString();
   }

   private String getPrincipalSchema() {
      return readPrincipalSchemaFile().stream().map(String::trim).collect(Collectors.joining());
   }

   private List<String> readPrincipalSchemaFile() {
      try {
         return Files.readAllLines(Paths.get(PRINCIPAL_SCHEMA_PATH));
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
