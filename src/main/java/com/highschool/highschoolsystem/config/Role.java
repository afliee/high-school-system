package com.highschool.highschoolsystem.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.highschool.highschoolsystem.config.Permission.*;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_WRITE,
                    ADMIN_DELETE
            )
    ),
    TEACHER(
            Set.of(
                    TEACHER_READ,
                    TEACHER_WRITE,
                    TEACHER_DELETE
            )
    ),
    STUDENT(
            Set.of(
                    STUDENT_READ,
                    STUDENT_WRITE,
                    STUDENT_DELETE
            )
    ),
    USER(
            Collections.emptySet()
    );


    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
