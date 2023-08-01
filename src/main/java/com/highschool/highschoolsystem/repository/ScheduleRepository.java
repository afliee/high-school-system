package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.LessonEntity;
import com.highschool.highschoolsystem.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, String> {
    @Query("select s.lessons from ScheduleEntity s")
    List<LessonEntity> findAllLessons();

    Optional<ScheduleEntity> findByClassEntity_Id(String id);

}
