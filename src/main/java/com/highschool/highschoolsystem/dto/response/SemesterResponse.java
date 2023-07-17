package com.highschool.highschoolsystem.dto.response;

import lombok.*;

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
