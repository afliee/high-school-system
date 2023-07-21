package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.LessonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<LessonEntity, String> {
    Page<?> findAllBySubjectId(String subjectId, Pageable pageable);

    List<LessonEntity> findAllByWeekSemesterIdAndSubjectId(String semesterId, String subjectId);

    List<LessonEntity> findAllBySubjectIdAndWeekSemesterIdAndWeekStartDateGreaterThanEqualAndWeekEndDateLessThanEqual(
            String subject_id, String week_semester_id, LocalDate week_startDate, LocalDate week_endDate
    );


    @Query(value = "" +
            "SELECT " +
            "l.id, " +
            "l.created_by, " +
            "l.created_date, " +
            "l.day_id, " +
            "l.end_date, " +
            "l.is_absent, " +
            "l.shift_id, " +
            "l.start_date, " +
            "l.subject_id, " +
            "l.updated_by, " +
            "l.updated_date, " +
            "l.week_id " +
            "FROM lession l " +
            "LEFT JOIN week w ON w.id = l.week_id " +
            "WHERE l.subject_id = ?1 AND w.semester_id = ?2 AND ((w.start_date BETWEEN ?3 AND ?4) OR (w.end_date BETWEEN ?5 AND ?6))",
            nativeQuery = true
    )
    List<LessonEntity> findAllBySubjectIdAndWeekSemesterIdAndWeekStartDateBetweenOrWeekEndDateBetween(
            String subject_id, String week_semester_id, LocalDate week_startDate, LocalDate week_startDate2, LocalDate week_endDate, LocalDate week_endDate2
    );


    Optional<LessonEntity> findTop1BySubjectIdAndWeekIdAndDayIdAndShiftId(String subjectId, String weekId, String dayId, String shiftId);
}
