package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.dto.response.SemesterResponse;
import com.highschool.highschoolsystem.entity.SemesterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends JpaRepository<SemesterEntity, String> {
//    find semester between start date and end date
    @Query(value = "select * from semester where ?1 between start_date and end_date", nativeQuery = true)
    Optional<SemesterEntity> findSemesterByDate(String date);

//    get element have current day between start date and end date limit 1
    Optional<SemesterEntity> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);

    Optional<SemesterEntity> findByStartDateAfterAndEndDateBefore(LocalDate startDate, LocalDate endDate);

    List<SemesterEntity> findAllByOrderByStartDateDesc();
}
