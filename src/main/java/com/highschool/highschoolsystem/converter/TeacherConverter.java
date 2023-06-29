package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.entity.TeacherEntity;

public class TeacherConverter {
    public static TeacherResponse toResponse(TeacherEntity teacher) {
        return TeacherResponse.builder()
                .name(teacher.getName())
                .fullName(teacher.getFullName())
                .avatar(teacher.getAvatar())
                .phone(teacher.getPhone())
                .address(teacher.getAddress())
                .cardId(teacher.getCardId())
                .birthday(teacher.getBirthday())
                .salary(teacher.getSalary())
                .email(teacher.getEmail())
                .build();
    }

    public static TeacherEntity toEntity(TeacherResponse teacherResponse) {
        return TeacherEntity.builder()
                .name(teacherResponse.getName())
                .fullName(teacherResponse.getFullName())
                .avatar(teacherResponse.getAvatar())
                .phone(teacherResponse.getPhone())
                .address(teacherResponse.getAddress())
                .cardId(teacherResponse.getCardId())
                .birthday(teacherResponse.getBirthday())
                .salary(teacherResponse.getSalary())
                .email(teacherResponse.getEmail())
                .build();
    }

    public static void to(TeacherResponse teacherResponse, TeacherEntity teacherEntity) {
        teacherEntity.setName(teacherResponse.getName());
        teacherEntity.setFullName(teacherResponse.getFullName());
        teacherEntity.setAvatar(teacherResponse.getAvatar());
        teacherEntity.setPhone(teacherResponse.getPhone());
        teacherEntity.setAddress(teacherResponse.getAddress());
        teacherEntity.setCardId(teacherResponse.getCardId());
        teacherEntity.setBirthday(teacherResponse.getBirthday());
        teacherEntity.setSalary(teacherResponse.getSalary());
        teacherEntity.setEmail(teacherResponse.getEmail());
    }
}
