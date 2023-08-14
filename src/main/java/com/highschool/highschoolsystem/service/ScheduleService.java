package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.DayConverter;
import com.highschool.highschoolsystem.converter.LessonConverter;
import com.highschool.highschoolsystem.converter.ShiftConverter;
import com.highschool.highschoolsystem.converter.SubjectConverter;
import com.highschool.highschoolsystem.dto.request.SchedulingRequest;
import com.highschool.highschoolsystem.dto.response.LessonResponse;
import com.highschool.highschoolsystem.dto.response.SchedulingResponse;
import com.highschool.highschoolsystem.dto.response.SubjectGroupByResponse;
import com.highschool.highschoolsystem.dto.response.SubjectResponse;
import com.highschool.highschoolsystem.entity.LessonEntity;
import com.highschool.highschoolsystem.entity.ScheduleEntity;
import com.highschool.highschoolsystem.entity.SubjectEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.LessonRepository;
import com.highschool.highschoolsystem.repository.LevelRepository;
import com.highschool.highschoolsystem.repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleService {
    private static final Logger logger = Logger.getLogger(ScheduleService.class.getName());
    @Autowired
    private LessonService lessonService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private ClassService classService;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private LessonRepository lessonRepository;

    public Map<String, SubjectGroupByResponse> getSubjectGroupedByLevel(String levelId) {
        var level = levelRepository.findById(levelId).orElseThrow(
                () -> new NotFoundException("Level not found")
        );

        var subjectsAvailable = this.findAllSubjectInsideLessons(scheduleRepository.findAllLessons());
        List<SubjectEntity> listSubject = level.getSubjects().stream().map(subject -> {
            if (!subjectsAvailable.contains(subject.getId())) {
                return subject;
            } else {
                return null;
            }
        }).toList();
        var subjects = SubjectConverter.toResponse(listSubject);
//        filter subjects not in subjectsAvailable

        Map<String, SubjectGroupByResponse> subjectGroupByResponseMap = new HashMap<>();
        subjects.forEach(subject -> {
            if (subject == null) {
                return;
            }

            var lessons = lessonRepository.findAllBySubjectId(subject.getId());

            var subjectByLevelResponse = SchedulingResponse.SubjectByLevelResponse.builder()
                    .id(subject.getId())
                    .name(subject.getName())
                    .teacher(subject.getTeacher())
                    .color(subject.getColor())
                    .lessons(lessons.stream().map(lesson -> {
                        return LessonResponse.builder()
                                .id(lesson.getId())
                                .shift(ShiftConverter.toResponse(lesson.getShift()))
                                .day(DayConverter.toResponse(lesson.getDay()))
                                .startDate(lesson.getStartDate())
                                .endDate(lesson.getEndDate())
                                .build();
                    }).toList())
                    .build();
            if (!subjectGroupByResponseMap.containsKey(subject.getDepartmentDetail().getId())) {
                var subjectGroupByResponse = SubjectGroupByResponse.builder()
                        .name(subject.getDepartment())
                        .subjects(new ArrayList<>(List.of(subjectByLevelResponse)))
                        .build();
                subjectGroupByResponseMap.put(subject.getDepartmentDetail().getId(), subjectGroupByResponse);
            } else {
                var subjectGroupByResponse = subjectGroupByResponseMap.get(subject.getDepartmentDetail().getId());
                subjectGroupByResponse.getSubjects().add(subjectByLevelResponse);
            }
        });
        return subjectGroupByResponseMap;
    }


    public void create(SchedulingRequest request) {
        List<String> lessonIds = request.getLessonIds();

        var lessons = lessonService.findAllByIdIn(lessonIds);
        var semester = semesterService.findByIdEntity(request.getSemesterId());
        var classEntity = classService.get(request.getClassId());

        var schedule = ScheduleEntity.builder()
                .isExpired(false)
                .expiredDate(semester.getEndDate())
                .semester(semester)
                .lessons(lessons)
                .classEntity(classEntity)
                .build();

        scheduleRepository.save(schedule);
    }

    public List<String> findAllSubjectInsideLessons(List<LessonEntity> lessons) {
        List<String> subjects = new ArrayList<>();
        lessons.forEach(lesson -> {
            var subject = lesson.getSubject();
            if (!subjects.contains(subject.getId())) {
                subjects.add(subject.getId());
            }
        });
        return subjects;
    }

//    find all subject inside lessons with return type List<SubjectEntity>
    public List<SubjectResponse> findAllSubjectInsideLessonsWithEntity(Set<LessonEntity> lessons) {
        var subjects = new ArrayList<SubjectEntity>();

        lessons.forEach(lesson -> {
            var subject = lesson.getSubject();
            if (!subjects.contains(subject)) {
                subjects.add(subject);
            }
        });

        return SubjectConverter.toResponse(subjects);
    }

    private List<ScheduleEntity> getAllScheduleByClassId(String classId) {
        return scheduleRepository.findAllByClassEntity_Id(classId).stream().filter(schedule -> !schedule.isExpired()).toList();
    }

    public List<SubjectResponse> getSubjectAvailable(String classId) {
        var schedule = getAllScheduleByClassId(classId).stream().findFirst().orElseThrow(
                () -> new NotFoundException("Schedule not found")
        );

        return findAllSubjectInsideLessonsWithEntity(schedule.getLessons());
    }

    public List<LessonResponse> getScheduleDetail(String classId, String semesterId, LocalDate start, LocalDate end) {
        var schedule = scheduleRepository.findAllByClassEntity_IdAndSemester_Id(classId, semesterId).stream().filter(scheduleEntity -> !scheduleEntity.isExpired()).findFirst().orElseThrow(
                () -> new NotFoundException("Schedule not found")
        );

        var lessons = schedule.getLessons().stream().filter(lesson -> {
            return lesson.getStartDate().isAfter(start.atStartOfDay()) && lesson.getEndDate().isBefore(end.atStartOfDay());
        }).toList();

        return LessonConverter.toResponse(lessons);
    }
}
