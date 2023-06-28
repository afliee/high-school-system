package com.highschool.highschoolsystem.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    @NotEmpty(message = "Username is required")
    @NotNull(message = "Username is required")
    private String username;

    @NotEmpty(message = "Full name is required")
    @NotBlank(message = "Full name is required")
    @Length(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
    private String fullName;

    @Email(message = "Email is invalid")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    @Length(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
