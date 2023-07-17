package com.highschool.highschoolsystem.converter;

import com.highschool.highschoolsystem.dto.response.DayResponse;
import com.highschool.highschoolsystem.entity.DayEntity;

public class DayConverter {
    public static DayResponse toResponse(DayEntity dayEntity) {
        return DayResponse.builder()
                .id(dayEntity.getId())
                .name(dayEntity.getDayName())
                .build();
    }
}
