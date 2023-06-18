package com.highschool.highschoolsystem.entity;

import com.highschool.highschoolsystem.config.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity<String> {
    @Column(unique = true)
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
