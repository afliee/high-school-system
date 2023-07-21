package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.DayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DayRepository extends JpaRepository<DayEntity, String> {
}
