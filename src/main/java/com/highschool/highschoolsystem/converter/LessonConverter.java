package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.LessonResponse;
import com.highschool.highschoolsystem.dto.response.SubjectResponse;
import com.highschool.highschoolsystem.entity.*;
import org.apache.commons.text.CaseUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class LessonConverter {
    public static LessonResponse toResponse(LessonEntity lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .day(DayConverter.toResponse(lesson.getDay()))
                .shift(ShiftConverter.toResponse(lesson.getShift()))
                .subject(SubjectConverter.toResponse(lesson.getSubject()))
                .build();
    }

    public static List<LessonResponse> toResponse(List<LessonEntity> lessons) {
        return lessons.stream().map(LessonConverter::toResponse).toList();
    }

    public static List<LessonResponse> toResponse(List<LessonEntity> lessons, String[] attrs) {
        return lessons.stream().map(lesson -> toResponse(lesson, attrs)).toList();
    }

    public static LessonResponse toResponse(LessonEntity lesson, String[] methodNames) {
        List<String> names = List.of(methodNames);
        List<Method> methods = List.of(lesson.getClass().getMethods());
        LessonResponse response = new LessonResponse();
        Class<?> responseClass = response.getClass();
        for (var method : methods) {
            if (names.contains(method.getName())) {
                try {
//                    String methodConverted = ConverterUtils.convertMethodName();

                    Method rootMethod = lesson.getClass().getMethod(method.getName());

                    Object value = rootMethod.invoke(lesson);

                    if (value == null) {
                        continue;
                    }

                    String className = value.getClass().getSimpleName();

                    value = convertClassName(className, value);
                    String fieldName = CaseUtils.toCamelCase(method.getName().substring(3).toLowerCase(), false, ' ');
                    Field field = responseClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(response, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }



    public static Object convertClassName(String className, Object value) {
        switch (className) {
            case "DayEntity" -> value = DayConverter.toResponse((DayEntity) value);
            case "ShiftEntity" -> value = ShiftConverter.toResponse((ShiftEntity) value);
            case "SubjectEntity" -> value = SubjectResponse.builder()
                    .name(((SubjectEntity) value).getName())
                    .id(((SubjectEntity) value).getId())
                    .color(((SubjectEntity) value).getColor())
                    .build();
            case "WeekEntity" -> value = WeekConverter.toResponse((WeekEntity) value, new String[] {
                    "id",
                    "startDate",
                    "endDate",
                    "weekIndex"
            });
            default -> {}
        }
        return value;
    }
}
