package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.entity.UserEntity;
import com.highschool.highschoolsystem.util.principal.UserPrincipal;

public class UserConverter {
    public static UserPrincipal toPrincipal(UserEntity userEntity) {
        return UserPrincipal.builder()
                .id(userEntity.getUserId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .build();
    }

    public static UserPrincipal toPrincipal(TeacherEntity teacherEntity) {
        return UserPrincipal.builder()
                .id(teacherEntity.getId())
                .username(teacherEntity.getName())
                .password(teacherEntity.getPassword())
                .role(teacherEntity.getRole())
                .build();
    }

    public static UserPrincipal toPrincipal(StudentEntity studentEntity) {
        return UserPrincipal.builder()
                .id(studentEntity.getId())
                .username(studentEntity.getName())
                .password(studentEntity.getPassword())
                .role(studentEntity.getRole())
                .build();
    }
}
