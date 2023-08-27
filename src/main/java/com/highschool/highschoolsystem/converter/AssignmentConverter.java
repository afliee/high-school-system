package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.AssignmentResponse;
import com.highschool.highschoolsystem.entity.AssignmentEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AssignmentConverter {
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static AssignmentResponse toResponse(AssignmentEntity assignment) {
        if (assignment == null) {
            return null;
        }

        return AssignmentResponse.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .teacher(assignment.getTeacher().getFullName())
                .description(assignment.getDescription())
                .startedDate(String.valueOf(assignment.getStartedDate()))
                .closedDate(String.valueOf(assignment.getClosedDate()))
                .points(assignment.getPoints())
                .build();
    }
}
