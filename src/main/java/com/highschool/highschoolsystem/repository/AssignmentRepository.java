package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, String> {
    List<AssignmentEntity> findAllBySubjectId(String subjectId);
}
