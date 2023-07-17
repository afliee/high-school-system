package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.ShiftResponse;
import com.highschool.highschoolsystem.entity.ShiftEntity;


public class ShiftConverter {
    public static ShiftResponse toResponse(ShiftEntity shift) {
        return ShiftResponse.builder()
                .id(shift.getId())
                .name(shift.getName())
                .startTime(shift.getStartTime())
                .endTime(shift.getEndTime())
                .build();
    }
}
