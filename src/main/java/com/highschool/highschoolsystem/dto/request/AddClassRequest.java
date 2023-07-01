package com.highschool.highschoolsystem.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddClassRequest {
    @NotEmpty(message = "Name is required")
    @NotNull(message = "Name is required")
    private String name;

    @NotEmpty(message = "Chairman is required")
    @NotNull(message = "Chairman is required")
    private String chairman;

    @NotNull(message = "Level is required")
    @NotEmpty(message = "Level is required")
    private String levelId;

    @NotNull(message = "Semester is required")
    @NotEmpty(message = "Semester is required")
    private String semesterId;

    @NotNull(message = "Students is required")
    private MultipartFile students;
}
