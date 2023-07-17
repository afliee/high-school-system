package com.highschool.highschoolsystem.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
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
