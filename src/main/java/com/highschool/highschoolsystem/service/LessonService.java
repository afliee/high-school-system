package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.LessonConverter;
import com.highschool.highschoolsystem.dto.request.LessonRequest;
import com.highschool.highschoolsystem.dto.response.LessonResponse;
import com.highschool.highschoolsystem.entity.LessonEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@NoArgsConstructor
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
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private SemesterService semesterService;

    public LessonEntity save(LessonEntity lessonEntity) {
        return lessonRepository.save(lessonEntity);
    }

    public List<LessonResponse> create(LessonRequest request) {
        Map<String, List<String>> dataProcessed = preProcessRequest(request);
//        log dataProcessed
        dataProcessed.forEach((key, value) -> {
            logger.info("dayId key: " + key);
            value.forEach(logger::info);
        });
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
        if (semesterId.equals("current")) {
            var semester = semesterService.findCurrentSemester();

            if (semester == null) {
                throw new NotFoundException("Semester not found");
            }

            semesterId = semester.getId();
        }

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

    public List<LessonEntity> findAllBySubjectIdIn(List<String> subjectIds) {
        return lessonRepository.findAllBySubjectIdIn(subjectIds);
    }

    public Set<LessonEntity> findAllByIdIn(List<String> lessonIds) {
        return lessonRepository.findAllByIdIn(lessonIds);
    }

    private Map<String, List<String>> preProcessRequest(LessonRequest request) {
        Map<String, List<String>> dataProcessed = new HashMap<>();

        var semester = semesterRepository.findById(request.getSemesterId()).orElseThrow(
                () -> new NotFoundException("Semester not found")
        );

        List<String> dayIds = new ArrayList<>(request.getDayIds());
        List<String> shiftIds = new ArrayList<>(request.getShiftIds());
        List<String> dayIdsChecked = new ArrayList<>();
        logger.info("before remove");
        logger.info("dayIds " + dayIds.toString());
        logger.info("shiftId " + shiftIds.toString());

        if (dayIds.size() != shiftIds.size()) {
            throw new RuntimeException("Day and shift must be the same size");
        }

        var lessonAvailable = lessonRepository.findAllBySubjectIdAndWeekSemesterId(request.getSubjectId(), request.getSemesterId());
        List<LessonEntity> lessons = new ArrayList<>();

        IntStream.range(0, dayIds.size()).forEach(index -> {
            var dayId = dayIds.get(index);
            var shiftId = shiftIds.get(index);

            var lessonExist = lessonRepository.findAllByDayIdAndShiftIdAndSubjectIdAndWeekSemesterId(dayId, shiftId, request.getSubjectId(), request.getSemesterId());

            if (lessonExist.size() > 0) {
                lessons.addAll(lessonExist);
                logger.info("lesson exist with index: " + index);
                dayIdsChecked.add(dayId);
            }
        });

        dayIdsChecked.forEach(dayId -> {
            int index = dayIds.indexOf(dayId);
            logger.info("remove index: " + index + " with dayId: " + dayId);
            dayIds.remove(index);
            shiftIds.remove(index);
        });
        logger.info("lessonAvailable size: " + lessonAvailable.size());
        logger.info("lessons size: " + lessons.size());
        lessonAvailable.removeAll(lessons);
        logger.info("lessonAvailable size: " + lessonAvailable.size());

        lessonAvailable.forEach(lesson -> {
            logger.info("day: " + lesson.getDay().getName() + " shift: " + lesson.getShift().getName());

            var schedules = lesson.getSchedules();
            schedules.forEach(schedule -> {
                schedule.getLessons().remove(lesson);
                scheduleRepository.save(schedule);
            });
            lessonRepository.delete(lesson);
        });

        logger.info("after remove");
        logger.info(dayIds.toString());
        logger.info(shiftIds.toString());

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

    public List<LessonResponse> getLessonTimeAvailable(String subjectId, String semesterId) {
        var lessons = lessonRepository.findAllBySubjectIdAndWeekSemesterId(subjectId, semesterId);
        return LessonConverter.toResponse(lessons);
    }

    public List<LessonEntity> getLessonToday(int limit) {
//        var today = LocalDateTime.now();
        var before2Today = LocalDateTime.now().minusDays(2);
        var after2Today = LocalDateTime.now().plusDays(2);
        var semester = semesterService.findCurrentSemesterEntity();

        if (semester == null) {
            return List.of();
        }

        if (limit == -1) {
            return lessonRepository.findAllByWeekSemesterIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(semester.getId(), before2Today, after2Today);
        }
        return lessonRepository.findAllByWeekSemesterIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(semester.getId(), before2Today, after2Today).stream().limit(limit).toList();
    }
}
