package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.SemesterConverter;
import com.highschool.highschoolsystem.dto.request.SemesterRequest;
import com.highschool.highschoolsystem.dto.response.SemesterResponse;
import com.highschool.highschoolsystem.entity.SemesterEntity;
import com.highschool.highschoolsystem.entity.WeekEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.ClassRepository;
import com.highschool.highschoolsystem.repository.SemesterRepository;
import com.highschool.highschoolsystem.repository.SubjectRepository;
import com.highschool.highschoolsystem.repository.WeekRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class SemesterService {
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private WeekRepository weekRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private ClassService classService;
    @Autowired
    private SubjectRepository subjectRepository;


    public SemesterResponse save(SemesterEntity semesterEntity) {
        var semester = semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(semesterEntity.getStartDate(), semesterEntity.getStartDate());
        System.out.println(semester);
        if (semester.isPresent()) {
            throw new RuntimeException("Semester is exist");
        }

//        create weeks through startDate and endDate
        var semesterSaved = semesterRepository.save(semesterEntity);

        var weeks = new ArrayList<WeekEntity>();
        var startDate = semesterEntity.getStartDate();
        var endDate = semesterEntity.getEndDate();

        int weekCount = 1;
//        count week
        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            var week = WeekEntity.builder()
                    .name("Week " + weekCount)
                    .startDate(startDate)
                    .endDate(startDate.plusDays(6))
                    .weekIndex(weekCount)
                    .semester(semesterEntity)
                    .build();
            weeks.add(week);
            startDate = startDate.plusDays(7);
            weekCount++;
        }

        weekRepository.saveAll(weeks);
        return SemesterConverter.toResponse(semesterSaved);
    }


    public List<SemesterResponse> findAll() {
        return semesterRepository.findAllByOrderByStartDateDesc().stream().map(SemesterConverter::toResponse).toList();
    }

    public SemesterResponse findCurrentSemester() {
        var currentDay = LocalDate.now();

        var semester = semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(currentDay, currentDay);

        if (semester.isEmpty()) {
            throw new NotFoundException("Semester not found");
        }

        return SemesterConverter.toResponse(semester.get());
    }

    public SemesterEntity findCurrentSemesterEntity() {
        var currentDay = LocalDate.now();

        return semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(currentDay, currentDay).orElse(null);
    }

    public SemesterResponse findById(String semesterId) {
        var semester = semesterRepository.findById(semesterId);

        if (semester.isEmpty()) {
            throw new NotFoundException("Semester not found");
        }

        return SemesterConverter.toResponse(semester.get());
    }

    public SemesterEntity findByIdEntity(String semesterId) {
        var semester = semesterRepository.findById(semesterId);

        if (semester.isEmpty()) {
            throw new NotFoundException("Semester not found");
        }

        return semester.get();
    }

    public SemesterResponse update(String semesterId, SemesterRequest request) {
        var semester = semesterRepository.findById(semesterId);

        if (semester.isEmpty()) {
            throw new NotFoundException("Semester not found");
        }

        var semesterEntity = semester.get();

        semesterEntity.setName(request.getName());
        semesterEntity.setStartDate(request.getStartDate());
        semesterEntity.setEndDate(request.getEndDate());

        var semesterSaved = semesterRepository.save(semesterEntity);

        return SemesterConverter.toResponse(semesterSaved);
    }

    @Transactional
    public void delete(String semesterId) {
        var semester = semesterRepository.findById(semesterId);

        if (semester.isEmpty()) {
            throw new NotFoundException("Semester not found");
        }

        var semesterEntity = semester.get();

        semesterEntity.getClasses().forEach(classEntity -> {
            classService.delete(classEntity.getId());
        });

        semesterEntity.getSchedules().forEach(scheduleEntity -> {
            var subjects = scheduleEntity.getSubjects();
            subjects.forEach(subjectEntity -> {
                subjectEntity.setSchedule(null);
                subjectRepository.save(subjectEntity);
            });
        });
        semesterRepository.delete(semester.get());
    }
}
