package com.highschool.highschoolsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AssignmentResponse {
    private String id;
    private String title;
    private String description;
    private String attachment;
    private String startedDate;
    private String closedDate;
    private String teacher;
    private String subject;
    private double points;
    private boolean isDue;
}
