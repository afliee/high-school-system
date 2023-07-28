package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, String> {
}
