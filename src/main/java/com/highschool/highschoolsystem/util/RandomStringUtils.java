package com.highschool.highschoolsystem.util;

public class RandomStringUtils {
    public static String randomAlphanumeric(int length) {
        String code = "";
        for (int i = 0; i < length; i++) {
            int random = (int) (Math.random() * 62);
            if (random < 10) {
                code += random;
            } else if (random < 36) {
                code += (char) (random + 55);
            } else {
                code += (char) (random + 61);
            }
        }
        return code;
    }
}
