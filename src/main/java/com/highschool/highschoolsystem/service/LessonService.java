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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@Service
public class LessonService {
    private static final Logger logger = Logger.getLogger(LessonService.class.getName());

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
        Map<String, List<String>> dataProcessed = preProcessRequest(request);
        var semester = semesterRepository.findById(request.getSemesterId()).orElseThrow(
                () -> new NotFoundException("Semester not found")
        );
        logger.info(semester.getName());
        var weeks = semester.getWeeks();
//        get days by Map<String, List<String>> dayIds
        var days = dayRepository.findAllById(request.getDayIds());
//        get shifts by Map<String, List<String>> shiftIds
//        var shifts = shiftRepository.findAllById(request.getShiftIds());
        var shiftIds = request.getShiftIds();
        System.out.println("days");
        days.forEach(day -> logger.info(day.getName()));
        System.out.println("weeks");
        weeks.forEach(week -> logger.info(week.getName()));
        var subject = subjectRepository.findById(request.getSubjectId()).orElseThrow(
                () -> new NotFoundException("Subject not found")
        );

        var lessons = new ArrayList<LessonEntity>();

        for (var week : weeks) {
//                keys of dataProcessed
            var dayIds = dataProcessed.keySet();
            for (var dayId : dayIds) {
                var day = dayRepository.findById(dayId).orElseThrow(
                        () -> new NotFoundException("Day not found")
                );
                var shiftIdsOfTheDay = dataProcessed.get(dayId);
                for (var shiftId : shiftIdsOfTheDay) {
                    var shift = shiftRepository.findById(shiftId).orElseThrow(
                            () -> new NotFoundException("Shift not found")
                    );
                    String dateName = day.getName().toUpperCase();
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

                    var lessonOptional = lessonRepository.findTop1BySubjectIdAndWeekIdAndDayIdAndShiftId(subject.getId(), week.getId(), day.getId(), shift.getId());
                    if (lessonOptional.isPresent()) {
                        throw new RuntimeException("Lesson already exists");
                    }
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

        var lessons = lessonRepository.findAllBySubjectIdAndWeekSemesterIdAndWeekStartDateBetweenOrWeekEndDateBetween(subjectId, semesterId, start, end, start, end);
        return LessonConverter.toResponse(lessons, new String[]{
                "id",
                "week",
                "subject",
                "shift",
                "startDate",
                "endDate"
        });
    }

    private Map<String, List<String>> preProcessRequest(LessonRequest request) {
        Map<String, List<String>> dataProcessed = new HashMap<>();

        List<String> dayIds = new ArrayList<>(request.getDayIds());
        List<String> shiftIds = new ArrayList<>(request.getShiftIds());

        int daySize = dayIds.size();

        IntStream.range(0, daySize).forEach(index -> {
            var dayId = dayIds.get(index);
            var shiftId = shiftIds.get(index);

            if (dataProcessed.containsKey(dayId)) {
                dataProcessed.get(dayId).add(shiftId);
            } else {
                var shiftIdsOfTheDay = new ArrayList<String>();
                shiftIdsOfTheDay.add(shiftId);
                dataProcessed.put(dayId, shiftIdsOfTheDay);
            }
        });

        return dataProcessed;
    }
}
