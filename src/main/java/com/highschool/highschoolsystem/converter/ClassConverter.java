package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.ClassResponse;
import com.highschool.highschoolsystem.entity.ClassEntity;

public class ClassConverter {
    public static ClassResponse toResponse(ClassEntity classEntity) {
        return ClassResponse.builder()
                .id(classEntity.getId())
                .name(classEntity.getName())
                .chairman(classEntity.getChairman() != null ? classEntity.getChairman().getName() : null)
                .present(classEntity.getPresent())
                .createdDate(classEntity.getCreatedDate().toString())
                .students(classEntity.getStudents().stream().map(StudentConverter::toResponse).toList())
                .build();
    }
}
