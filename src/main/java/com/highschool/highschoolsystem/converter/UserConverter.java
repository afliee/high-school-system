package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.entity.UserEntity;
import com.highschool.highschoolsystem.util.principal.UserPrincipal;

public class UserConverter {
    public static UserPrincipal toPrincipal(UserEntity userEntity) {
        return UserPrincipal.builder()
                .id(userEntity.getUserId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .role(userEntity.getRole().name())
                .build();
    }
}
