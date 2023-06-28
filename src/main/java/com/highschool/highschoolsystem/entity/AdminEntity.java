package com.highschool.highschoolsystem.entity;

import com.highschool.highschoolsystem.config.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminEntity extends BaseEntity<String> {
    private String username;
    private String password;
    private String fullName;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;
}
