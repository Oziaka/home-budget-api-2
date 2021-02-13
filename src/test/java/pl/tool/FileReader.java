package pl.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileReader {
   public FileReader(String filePath) {
      this.filePath = filePath;
   }

   private String filePath;

   public byte[] getFile() {
      try {
         return Files.readAllBytes(Paths.get(filePath));
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }

}
