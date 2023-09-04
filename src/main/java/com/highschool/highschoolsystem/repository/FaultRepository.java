package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.FaultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaultRepository extends JpaRepository<FaultEntity, String> {
    List<FaultEntity> findAllByStudentIdAndSubject(String studentId, String subjectId);
    void deleteAllByStudentId(String studentId);
}
