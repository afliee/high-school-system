package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.LessonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LessonRepository extends JpaRepository<LessonEntity, String> {
    Page<?> findAllBySubjectId(String subjectId, Pageable pageable);
    List<LessonEntity> findAllByWeekSemesterIdAndSubjectId(String semesterId, String subjectId);

    List<LessonEntity> findAllBySubjectIdAndWeekSemesterIdAndWeekStartDateGreaterThanEqualAndWeekEndDateLessThanEqual(
            String subject_id, String week_semester_id, LocalDate week_startDate, LocalDate week_endDate
    );
}
