package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.converter.ShiftConverter;
import com.highschool.highschoolsystem.dto.request.ShiftRequest;
import com.highschool.highschoolsystem.dto.response.ShiftResponse;
import com.highschool.highschoolsystem.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;

    public ShiftResponse create(ShiftRequest shiftRequest) {
        var shift = shiftRepository.findTop1ByStartTimeAndEndTimeIsBetween(shiftRequest.getStartTime(), shiftRequest.getEndTime());
        if (shift.isPresent()) {
            throw new RuntimeException("Shift is already exist");
        }

        var entity = ShiftConverter.toEntity(shiftRequest);
        var saved = shiftRepository.save(entity);
        return ShiftConverter.toResponse(saved);
    }

    public List<ShiftResponse> getAll() {
//        apply sort
        var sort = Sort.by(Sort.Direction.ASC, "startTime");
        var shifts = shiftRepository.findAll(sort);
        return ShiftConverter.toResponse(shifts);
    }
}
