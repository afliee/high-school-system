package com.highschool.highschoolsystem.converter;

public class ConverterUtils {
    public static String convertMethodName(String methodName) {
        if (!methodName.contains("get")) {
            return "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        }

        if (methodName.startsWith("get")) {
            return methodName.replaceFirst("get", "set");
        } else {
            return methodName.replaceFirst("set", "get");
        }
    }
}
