package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.ClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, String> {
//    find all class by semester id with pagerequest
    Page<ClassEntity> findAllBySemesterId(String semesterId, Pageable pageRequest);

    @Query("select c from ClassEntity c where c.semester.id = ?1 and c.level.id = ?2")
    Page<ClassEntity> findAllBySemesterIdAndLevelId(String semesterId, String levelId, Pageable pageRequest);
    Optional<ClassEntity> findByChairmanId(String chairmanId);

    @Query(value = "select s from ClassEntity s where s.level.id = ?1 and (s.semester.startDate <= ?2 and s.semester.endDate >= ?2)")
    List<ClassEntity> findAllByLevelIdAndCurrentSemester(String levelId, LocalDate date);
}
