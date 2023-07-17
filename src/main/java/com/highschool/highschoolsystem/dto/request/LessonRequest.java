package com.highschool.highschoolsystem.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonRequest {
    @JsonProperty("dayIds")
    private List<String> dayIds;
    private String subjectId;
    private String semesterId;
    @JsonProperty("shiftIds")
    private List<String> shiftIds;
}
