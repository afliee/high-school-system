package com.highschool.highschoolsystem.auth;

import jakarta.validation.constraints.Max;
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
@AllArgsConstructor
@NoArgsConstructor
public class ForgotConfirmRequest {
    @NotEmpty(message = "Username is required")
    @NotNull(message = "Username is required")
    private String username;

    @NotEmpty(message = "New password is required")
    @Length(min = 8, max = 50, message = "Password must be between 8 and 50 characters long")
    @NotNull(message = "New password is required")
    private String newPassword;

    @Length(min = 6, max = 6, message = "Code must be 6 characters long")
    @NotEmpty(message = "Code is required")
    private String code;
}
