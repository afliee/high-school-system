package com.highschool.highschoolsystem.auth;

import com.highschool.highschoolsystem.config.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String username;
    private String fullName;
    private String email;
    private String password;
}
