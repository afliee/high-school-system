package com.highschool.highschoolsystem.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SemesterRequest {
    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name is required")
    private String name;

    @NotNull(message = "Start date is required")
    @NotEmpty(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @NotEmpty(message = "End date is required")
    private LocalDate endDate;
}
