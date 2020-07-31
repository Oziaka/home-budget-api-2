package pl.tool;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class Tool {

    public static String randomString() {
        return RandomStringUtils.randomAlphabetic(10);

    }

    public static String randomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static Long randomLong() {
        return random.nextLong();
    }

    private static final Random random = new Random();

}
