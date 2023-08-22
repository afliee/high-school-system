package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.ClassEntity;
import com.highschool.highschoolsystem.entity.LessonEntity;
import com.highschool.highschoolsystem.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, String> {
    @Query("select s.lessons from ScheduleEntity s")
    List<LessonEntity> findAllLessons();

    List<ScheduleEntity> findAllByClassEntity_Id(String id);
    List<ScheduleEntity> findAllByClassEntity_IdAndSemester_Id(String classId, String semesterId);
}
