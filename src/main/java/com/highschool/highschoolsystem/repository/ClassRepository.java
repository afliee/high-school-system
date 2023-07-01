package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<ClassEntity, String> {
}
