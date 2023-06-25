package com.highschool.highschoolsystem.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    @NotEmpty(message = "Email is required")
    @NotNull(message = "Email is required")
    private String username;

    @NotEmpty(message = "Email is required")
    @NotNull(message = "Email is required")
    @Length(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;
}
