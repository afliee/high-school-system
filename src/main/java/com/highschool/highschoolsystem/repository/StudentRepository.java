package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, String> {
    Optional<StudentEntity> findByName(String name);
}
