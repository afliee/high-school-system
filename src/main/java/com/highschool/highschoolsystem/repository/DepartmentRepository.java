package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, String> {
}
