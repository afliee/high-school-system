package com.highschool.highschoolsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LessonResponse {
    private String id;
    private DayResponse day;
    private SubjectResponse subject;
    private ShiftResponse shift;
    private boolean isAbsent;
    private WeekResponse week;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
