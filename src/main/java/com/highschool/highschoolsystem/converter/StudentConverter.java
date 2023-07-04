package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.StudentResponse;
import com.highschool.highschoolsystem.entity.StudentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StudentConverter {
    public static StudentResponse toResponse(StudentEntity studentEntity) {
        return StudentResponse.builder()
                .id(studentEntity.getId())
                .name(studentEntity.getName())
                .fullName(studentEntity.getFullName())
                .email(studentEntity.getEmail())
                .avatar(studentEntity.getAvatar())
                .location(studentEntity.getLocation())
                .cardId(studentEntity.getCardId())
                .enteredDate(studentEntity.getEnteredDate())
                .build();
    }
}
