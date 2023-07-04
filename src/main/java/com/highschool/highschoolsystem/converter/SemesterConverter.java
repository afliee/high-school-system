package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.request.SemesterRequest;
import com.highschool.highschoolsystem.dto.response.SemesterResponse;
import com.highschool.highschoolsystem.entity.SemesterEntity;

import java.time.LocalDate;

public class SemesterConverter {
    public static SemesterEntity toEntity(SemesterRequest request) {
        return SemesterEntity.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }

    public static SemesterResponse toResponse(SemesterEntity entity) {
        var currentDay = LocalDate.now();
        boolean isCurrent = currentDay.isEqual(entity.getStartDate()) ||
                            currentDay.isEqual(entity.getEndDate()) ||
                            (currentDay.isAfter(entity.getStartDate()) && currentDay.isBefore(entity.getEndDate()));

        return SemesterResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .isCurrent(isCurrent)
                .build();
    }
}
