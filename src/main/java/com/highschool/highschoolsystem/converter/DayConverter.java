package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.DayResponse;
import com.highschool.highschoolsystem.entity.DayEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DayConverter {
    private static final Logger logger = Logger.getLogger(DayConverter.class.getName());
    public static DayResponse toResponse(DayEntity dayEntity) {
        return DayResponse.builder()
                .id(dayEntity.getId())
                .name(dayEntity.getName())
                .build();
    }

    public static DayResponse toResponse(DayEntity day, String[] attrs) {
//        logger.info(day.getName());
        List<String> attributes = List.of(attrs);
        List<Field> rootFields = new ArrayList<>(List.of(day.getClass().getDeclaredFields()));
        rootFields.addAll(Arrays.asList(day.getClass().getSuperclass().getDeclaredFields()));
        List<Field> fields = rootFields.stream()
                .filter(field -> attributes.contains(field.getName()))
                .toList();

        Map<String, Object> fieldMap = fields.stream()
                .collect(Collectors.toMap(
                        Field::getName,
                        field -> {
                            try {
                                Method f = day.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                                return f.invoke(day);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                ));
        DayResponse response = new DayResponse();

        fieldMap.forEach((key, value) -> {
            try {
                Field field = response.getClass().getDeclaredField(key);
                field.setAccessible(true);
                field.set(response, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        return response;
    }

    public static List<DayResponse> toResponse(List<DayEntity> days, String[] attr) {
        return days.stream()
                .map(day -> toResponse(day, attr))
                .toList();
    }
}
