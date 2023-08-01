package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.ClassResponse;
import com.highschool.highschoolsystem.dto.response.StudentResponse;
import com.highschool.highschoolsystem.entity.ClassEntity;
import org.springframework.data.domain.Page;

import java.util.List;

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

    public static ClassResponse toResponse(ClassEntity classEntity, long limit) {
        return ClassResponse.builder()
                .id(classEntity.getId())
                .name(classEntity.getName())
                .hasSchedule(classEntity.getSchedule() != null)
                .chairman(classEntity.getChairman() != null ? classEntity.getChairman().getName() : null)
                .present(classEntity.getPresent())
                .createdDate(classEntity.getCreatedDate().toString())
                .students(classEntity.getStudents().stream().limit(limit).map(StudentConverter::toResponse).toList())
                .build();
    }

    public static ClassResponse toResponse(ClassEntity classEntity, List<StudentResponse> content, Page<StudentResponse> studentPage) {
        return ClassResponse.builder()
                .id(classEntity.getId())
                .name(classEntity.getName())
                .present(classEntity.getPresent())
                .chairman(classEntity.getChairman() != null ? classEntity.getChairman().getFullName() : null)
                .createdDate(classEntity.getCreatedDate().toString())
                .students(content)
                .studentPage(studentPage)
                .build();
    }

    public static List<ClassResponse> toResponse(List<ClassEntity> classEntities) {
        return classEntities.stream().map(ClassConverter::toResponse).toList();
    }
}
