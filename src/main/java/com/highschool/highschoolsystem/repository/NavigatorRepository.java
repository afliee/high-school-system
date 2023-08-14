package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.NavigatorEntity;
import com.highschool.highschoolsystem.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NavigatorRepository extends JpaRepository<NavigatorEntity, String> {
    Optional<NavigatorEntity> findByStudent_Id(String studentId);
    Optional<NavigatorEntity> findByStudent_Name(String studentName);
}
