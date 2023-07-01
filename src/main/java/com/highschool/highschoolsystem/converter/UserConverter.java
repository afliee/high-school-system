package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.entity.AdminEntity;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.entity.UserEntity;
import com.highschool.highschoolsystem.util.principal.UserPrincipal;

import java.util.ArrayList;
import java.util.List;

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

    public static UserPrincipal toPrincipal(AdminEntity adminEntity) {
        return UserPrincipal.builder()
                .id(adminEntity.getId())
                .username(adminEntity.getUsername())
                .password(adminEntity.getPassword())
                .role(adminEntity.getRole())
                .build();
    }

    public static UserEntity toEntity(StudentEntity student) {
        return UserEntity.builder()
                .userId(student.getId())
                .username(student.getName())
                .password(student.getPassword())
                .role(student.getRole())
                .build();
    }

    public static List<UserPrincipal> toPrincipal(List<StudentEntity> students) {
        List<UserPrincipal> users = new ArrayList<>();

        for (StudentEntity student : students) {
            users.add(toPrincipal(student));
        }
        return users;
    }

    public static List<UserEntity> toEntities(List<StudentEntity> students) {
        List<UserEntity> users = new ArrayList<>();

        for (StudentEntity student : students) {
            users.add(toEntity(student));
        }
        return users;
    }
}
