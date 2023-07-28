package com.highschool.highschoolsystem.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequest {
    @NotNull(message = "Subject name is required")
    @NotEmpty(message = "Subject name is required")
    private String name;

    @NotNull(message = "Subject description is required")
    @NotEmpty(message = "Subject description is required")
    private String description;

    @NotNull(message = "Teacher id is required")
    @NotEmpty(message = "Teacher id is required")
    private String teacherId;

    @NotNull(message = "Department id is required")
    @NotEmpty(message = "Department id is required")
    private String departmentId;

    @NotNull(message = "Level id is required")
    @NotEmpty(message = "Level id is required")
    private String levelId;
}
