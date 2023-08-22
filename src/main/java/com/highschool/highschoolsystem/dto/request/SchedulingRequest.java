package com.highschool.highschoolsystem.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchedulingRequest {
    List<String> lessonIds;

    @NotNull(message = "Semester id must not be null")
    @NotEmpty(message = "Semester id must not be empty")
    private String semesterId;

    @NotNull(message = "Level id must not be null")
    @NotEmpty(message = "Level id must not be empty")
    private String levelId;

    @NotNull(message = "Class id must not be null")
    @NotEmpty(message = "Class id must not be empty")
    private String classId;

    private List<String> subjectIds;
}
