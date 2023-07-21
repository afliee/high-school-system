package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.request.ShiftRequest;
import com.highschool.highschoolsystem.dto.response.ShiftResponse;
import com.highschool.highschoolsystem.entity.ShiftEntity;

import java.util.List;


public class ShiftConverter {
    public static ShiftResponse toResponse(ShiftEntity shift) {
        return ShiftResponse.builder()
                .id(shift.getId())
                .name(shift.getName())
                .startTime(shift.getStartTime())
                .endTime(shift.getEndTime())
                .build();
    }

    public static List<ShiftResponse> toResponse(List<ShiftEntity> shifts) {
        return shifts.stream()
                .map(ShiftConverter::toResponse)
                .toList();
    }

    public static ShiftEntity toEntity(ShiftRequest request) {
        return ShiftEntity.builder()
                .name(request.getName())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
    }
}
