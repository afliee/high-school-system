package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.WeekResponse;
import com.highschool.highschoolsystem.entity.WeekEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeekConverter {
    public static List<WeekResponse> toResponse(List<WeekEntity> weeks, String[] attrs) {
        return weeks.stream().map(week -> toResponse(week, attrs)).toList();
    }

    public static WeekResponse toResponse(WeekEntity week, String[] methodNames) {
        List<String> names = List.of(methodNames);
//        join two array to one
        List<Field> fields = new ArrayList<>(List.of(week.getClass().getDeclaredFields()));

        fields.addAll(List.of(week.getClass().getSuperclass().getDeclaredFields()));
        Class<?> rootClass = week.getClass();
        WeekResponse response = new WeekResponse();


        for (Field field : fields) {
            if (names.contains(field.getName())) {
                try {
                    String fieldName = field.getName();
                    String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = rootClass.getMethod(methodName);
                    Object value = method.invoke(week);
                    if (value == null) {
                        continue;
                    }

                    Field responseField = response.getClass().getDeclaredField(fieldName);
                    responseField.setAccessible(true);
                    responseField.set(response, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return response;
    }
}
