package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.DayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayRepository extends JpaRepository<DayEntity, String> {
}
