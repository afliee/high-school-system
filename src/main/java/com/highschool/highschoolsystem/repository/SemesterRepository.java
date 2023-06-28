package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.SemesterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<SemesterEntity, String> {
}
