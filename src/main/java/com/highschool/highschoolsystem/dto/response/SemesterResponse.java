package com.highschool.highschoolsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SemesterResponse {
    private String id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCurrent;
}
