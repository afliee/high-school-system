package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.ShiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<ShiftEntity, String> {
//    check if startTime or endTime with time was exist in database

    @Query(
            value = "SELECT * FROM shift WHERE (start_time between ?1 and ?2) or (end_time between ?1 and ?2) LIMIT 1;",
            nativeQuery = true
    )
    Optional<ShiftEntity> findTop1ByStartTimeAndEndTimeIsBetween(LocalTime startTime, LocalTime endTime);

    Optional<ShiftEntity> findByName(String name);
}
