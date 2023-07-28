package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.dto.request.SchedulingRequest;
import com.highschool.highschoolsystem.entity.LessonEntity;
import com.highschool.highschoolsystem.entity.ScheduleEntity;
import com.highschool.highschoolsystem.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private ClassService classService;

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
//        schedule and lessons has a many-to-many relationship
//        so we need to save the schedule first
//        then save the lessons
        lessons.forEach(lesson -> {
            lesson.getSchedules().add(schedule);
            lessonService.save(lesson);
        });
    }
}
