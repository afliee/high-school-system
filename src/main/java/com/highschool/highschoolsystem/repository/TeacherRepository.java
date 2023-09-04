package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<TeacherEntity, String> {
        Optional<TeacherEntity> findByName(String name);
        Optional<TeacherEntity> findByFullNameContainsIgnoreCase(String query);
        List<TeacherEntity> findByFullNameContainingIgnoreCase(String query);
}
