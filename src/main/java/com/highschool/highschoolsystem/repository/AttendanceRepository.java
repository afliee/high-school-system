package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, String> {
    List<AttendanceEntity> findAllByClassEntity_Id(String classId);
}
