package com.highschool.highschoolsystem.auth;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Password is required")
    @Min(value = 8, message = "Password must be at least 8 characters")
    @Length(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    private String password;

    @NotEmpty(message = "Role is required")
    private String role;
}
