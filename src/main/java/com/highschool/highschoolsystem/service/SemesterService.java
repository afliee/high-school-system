package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.SemesterConverter;
import com.highschool.highschoolsystem.dto.response.SemesterResponse;
import com.highschool.highschoolsystem.entity.SemesterEntity;
import com.highschool.highschoolsystem.entity.WeekEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.SemesterRepository;
import com.highschool.highschoolsystem.repository.WeekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SemesterService {
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private WeekRepository weekRepository;

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
}
