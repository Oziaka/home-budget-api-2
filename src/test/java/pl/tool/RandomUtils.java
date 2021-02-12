package pl.tool;

import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.util.Random;

public class RandomUtils {

   private static final Random random = new Random();

   public static String randomString() {
      return RandomStringUtils.randomAlphabetic(10);

   }

   public static String randomString(int length) {
      return RandomStringUtils.randomAlphabetic(length);
   }

   public static Long randomLong() {
      return random.nextLong();
   }

   public static BigDecimal randomBigDecimal() {
      return BigDecimal.valueOf(Double.parseDouble(RandomStringUtils.randomNumeric(2) + "." + RandomStringUtils.randomNumeric(2)));
   }
}
