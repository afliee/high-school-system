package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.SemesterConverter;
import com.highschool.highschoolsystem.dto.response.SemesterResponse;
import com.highschool.highschoolsystem.entity.SemesterEntity;
import com.highschool.highschoolsystem.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterService {
    @Autowired
    private SemesterRepository semesterRepository;

    public SemesterResponse save(SemesterEntity semesterEntity) {
        var semester = semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(semesterEntity.getStartDate(), semesterEntity.getStartDate());
        System.out.println(semester);
        if (semester.isPresent()) {
            throw new RuntimeException("Semester is exist");
        }
        return SemesterConverter.toResponse(semesterRepository.save(semesterEntity));
    }


    public List<SemesterResponse> findAll() {
        return semesterRepository.findAllByOrderByStartDateDesc().stream().map(SemesterConverter::toResponse).toList();
    }
}
