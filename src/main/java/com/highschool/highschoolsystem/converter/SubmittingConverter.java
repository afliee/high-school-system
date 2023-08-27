package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.SubmittingResponse;
import com.highschool.highschoolsystem.entity.Submitting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SubmittingConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public static SubmittingResponse toResponse(Submitting submitting) {
        if (submitting == null) {
            return null;
        }

        return SubmittingResponse.builder()
                .id(submitting.getId())
                .student(StudentConverter.toResponse(submitting.getStudent()))
                .file(submitting.getFile())
                .score(submitting.getScore())
                .comment(submitting.getComment())
                .createdAt(submitting.getAssignment().getStartedDate())
                .updatedAt(submitting.getTurnedAt())
                .isTurnedLate(submitting.isTurnedLate())
                .totalScore(submitting.getAssignment().getPoints())
                .assignment(AssignmentConverter.toResponse(submitting.getAssignment()))
                .status(submitting.getStatus())
                .build();
    }

    public static List<SubmittingResponse> toResponse(List<Submitting> submittingList) {
        return submittingList.stream().map(SubmittingConverter::toResponse).toList();
    }
}
