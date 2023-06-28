package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.ShiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<ShiftEntity, String> {
}
