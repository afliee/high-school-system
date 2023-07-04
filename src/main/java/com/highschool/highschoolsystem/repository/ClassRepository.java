package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.ClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<ClassEntity, String> {
//    find all class by semester id with pagerequest
    Page<ClassEntity> findAllBySemesterId(String semesterId, Pageable pageRequest);
}
