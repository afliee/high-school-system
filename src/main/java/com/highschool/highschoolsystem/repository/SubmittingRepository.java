package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.Submitting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmittingRepository extends JpaRepository<Submitting, String> {
    Optional<Submitting> findByAssignmentIdAndStudentId(String assignmentId, String studentId);
}
