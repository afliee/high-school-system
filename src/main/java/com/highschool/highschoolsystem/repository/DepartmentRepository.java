package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, String> {
    Optional<DepartmentEntity> findByName(String name);
}
