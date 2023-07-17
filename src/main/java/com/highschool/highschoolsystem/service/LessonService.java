package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.LessonConverter;
import com.highschool.highschoolsystem.dto.request.LessonRequest;
import com.highschool.highschoolsystem.dto.response.LessonResponse;
import com.highschool.highschoolsystem.entity.LessonEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LessonService {
    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private LessonRepository lessonRepository;

    public List<LessonResponse> create(LessonRequest request) {
        var semester = semesterRepository.findById(request.getSemesterId()).orElseThrow(
                () -> new NotFoundException("Semester not found")
        );

        var weeks = semester.getWeeks();
//        get days by Map<String, List<String>> dayIds
        var days = dayRepository.findAllById(request.getDayIds());
//        get shifts by Map<String, List<String>> shiftIds
        var shifts = shiftRepository.findAllById(request.getShiftIds());

        var subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(
                () -> new NotFoundException("Subject not found")
        );

        var lessons = new ArrayList<LessonEntity>();


        for (var week : weeks) {
            for (var day : days) {
                for (var shift : shifts) {
//                    find date of day in week begin startDate and end with endDate
                    String dateName = day.getDayName().toUpperCase();
                    LocalDateTime date = week.getStartDate().atStartOfDay();
                    while (!dateName.equals(date.getDayOfWeek().name())) {
                        date = date.plusDays(1);

                        if (date.isAfter(week.getEndDate().atStartOfDay())) {
                            break;
                        }
                    }

                    if (date.isAfter(week.getEndDate().atStartOfDay())) {
                        break;
                    }

//                                        set time in shift into date
                    var startTimeDate = date.plusHours(shift.getStartTime().getHour());
                    startTimeDate = startTimeDate.plusMinutes(shift.getStartTime().getMinute());

                    var endTimeDate = date.plusHours(shift.getEndTime().getHour());
                    endTimeDate = endTimeDate.plusMinutes(shift.getEndTime().getMinute());

                    var lesson = LessonEntity.builder()
                            .week(week)
                            .day(day)
                            .shift(shift)
                            .subject(subject)
                            .isAbsent(false)
                            .startDate(startTimeDate)
                            .endDate(endTimeDate)
                            .build();
                    lessons.add(lesson);
                }
            }
        }

        lessonRepository.saveAll(lessons);
        return LessonConverter.toResponse(lessons);
    }

    public List<?> get(String semesterId, String subjectId) {

        var lessons = lessonRepository.findAllByWeekSemesterIdAndSubjectId(semesterId, subjectId);
        return LessonConverter.toResponse(lessons);
    }

    public List<LessonResponse> get(String subjectId, String semesterId, LocalDate start, LocalDate end, boolean isDetail) {
        if (isDetail) {
            var lessons = lessonRepository.findAllBySubjectIdAndWeekSemesterIdAndWeekStartDateGreaterThanEqualAndWeekEndDateLessThanEqual(subjectId, semesterId, start, end);
            return LessonConverter.toResponse(lessons);
        }

        var lessons = lessonRepository.findAllBySubjectIdAndWeekSemesterIdAndWeekStartDateGreaterThanEqualAndWeekEndDateLessThanEqual(subjectId, semesterId, start, end);
        return LessonConverter.toResponse(lessons, new String[]{
                "getId",
                "getWeek",
                "getSubject",
                "getShift"
        });
    }
}
