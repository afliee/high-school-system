package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.ScheduleResponse;
import com.highschool.highschoolsystem.entity.ScheduleEntity;

public class ScheduleConverter {
    public static ScheduleResponse toResponse(ScheduleEntity schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .isExpired(schedule.isExpired())
                .expiredDate(String.valueOf(schedule.getExpiredDate()))
                .lessons(LessonConverter.toResponse(schedule.getLessons().stream().toList()))
                .subjects(SubjectConverter.toResponse(schedule.getSubjects().stream().toList()))
                .build();
    }
}
