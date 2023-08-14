package com.highschool.highschoolsystem.config;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_DELETE("admin:delete"),

    TEACHER_READ("teacher:read"),
    TEACHER_WRITE("teacher:write"),
    TEACHER_DELETE("teacher:delete"),

    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    STUDENT_DELETE("student:delete");

    private final String permission;

    public String getPermission() {
        return permission;
    }
}
