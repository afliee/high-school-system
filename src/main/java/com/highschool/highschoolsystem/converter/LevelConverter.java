package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.request.LevelRequest;
import com.highschool.highschoolsystem.dto.response.LevelResponse;
import com.highschool.highschoolsystem.entity.LevelEntity;
import com.highschool.highschoolsystem.entity.SubjectEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LevelConverter {
    public static LevelResponse toResponse(LevelEntity level) {
        return LevelResponse.builder()
                .id(level.getId())
                .name(level.getName())
                .levelNumber(level.getLevelNumber())
                .subjects(level.getSubjects() != null ?  SubjectConverter.toResponse(level.getSubjects().stream().toList()) : null)
                .classes(level.getClasses() != null ?  ClassConverter.toResponse(level.getClasses().stream().toList()) : null)
                .build();
    }

    public static LevelResponse toResponse(LevelEntity level, String[] attributes) {
        List<String> attributeList = List.of(attributes);

        List<Field> fields = new ArrayList<>(List.of(level.getClass().getDeclaredFields()));
        fields.addAll(List.of(level.getClass().getSuperclass().getDeclaredFields()));

        fields.removeIf(field -> !attributeList.contains(field.getName()));

        LevelResponse levelResponse = new LevelResponse();
        System.out.println("LevelConverter.toResponse");
        fields.forEach(field -> {
            try {
                field.setAccessible(true);
                Object value = field.get(level);
//                get type of field

                Field levelResponseField = levelResponse.getClass().getDeclaredField(field.getName());
                levelResponseField.setAccessible(true);
                levelResponseField.set(levelResponse, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return levelResponse;
    }

    public static List<LevelResponse> toResponse(List<LevelEntity> levels, String[] attributes) {
        return levels.stream().map(level -> toResponse(level, attributes)).toList();
    }

    public static List<LevelResponse> toResponse(List<LevelEntity> levels) {
        return levels.stream().map(LevelConverter::toResponse).toList();
    }

    private static Object convertResponse(String className, Object value) {
        switch (className) {
            case "LevelEntity" -> value = LevelConverter.toResponse((LevelEntity) value);
            case "SubjectEntity" -> value = SubjectConverter.toResponse((SubjectEntity) value);
            default -> {
            }
        }
        return value;
    }

    public static LevelEntity toEntity(LevelRequest levelRequest) {
        return LevelEntity.builder()
                .levelNumber(levelRequest.getLevelNumber())
                .name(levelRequest.getName())
                .build();
    }
}
