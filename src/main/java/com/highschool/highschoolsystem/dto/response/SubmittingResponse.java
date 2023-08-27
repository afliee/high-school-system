package com.highschool.highschoolsystem.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.highschool.highschoolsystem.config.SubmitStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SubmittingResponse {
    private String id;
    private StudentResponse student;
    private AssignmentResponse assignment;
    private String file;
    private double score;
    private String comment;

    private boolean isTurnedLate;
    private double totalScore;
    private SubmitStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
}
