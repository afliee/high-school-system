package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.ShiftConverter;
import com.highschool.highschoolsystem.dto.request.ShiftRequest;
import com.highschool.highschoolsystem.dto.response.ShiftResponse;
import com.highschool.highschoolsystem.repository.ShiftRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private DayService dayService;

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

    public Map<String, Boolean> getAvailableTime(String subjectId, String semesterId) {
        var days = dayService.getAllDays();
        var shifts = this.getAll();

        var lessonTimeAvailable = lessonService.getLessonTimeAvailable(subjectId, semesterId);
        if (lessonTimeAvailable.size() > 0) {
            Map<String, Boolean> lessonTimeAvailablePosition = new HashMap<>();
            for (int i = 0; i < days.size(); i++) {
                for (int j = 0; j < shifts.size(); j++) {
                    var shift = shifts.get(j);
                    var day = days.get(i);

                    if (lessonTimeAvailable.stream().anyMatch(lesson -> lesson.getDay().getId().equals(day.getId()) && lesson.getShift().getId().equals(shift.getId()))) {
                        lessonTimeAvailablePosition.put(i + "-" + j, true);
                    } else {
                        lessonTimeAvailablePosition.put(i + "-" + j, false);
                    }
                }
            }
            return lessonTimeAvailablePosition;
        }
        return null;
    }
}
