package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.SubjectEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.dto.response.SubjectGroupByResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<SubjectEntity, String> {
    Optional<SubjectEntity> findByTeacher(TeacherEntity teacher);
    List<SubjectEntity> findAllByIdIn(List<String> ids);
    List<SubjectEntity> findAllByNameContainingOrDepartment_Id(String name, String departmentId);
    List<SubjectEntity> findAllByNameContaining(String name);
    List<SubjectEntity> findAllByDepartment_Id(String departmentId);
    Page<SubjectEntity> findAllByNameContainingAndTeacherContaining(String name, String teacher, Pageable pageable);
    Page<SubjectEntity> findAllByNameContainingAndTeacher_FullNameContaining(String name, String teacherName, Pageable pageable);
    Page<SubjectEntity> findAllByNameContaining(String name, Pageable pageable);
//    select all subject group by department
    @Query("SELECT s FROM SubjectEntity s WHERE s.teacher.fullName LIKE %?1% OR s.teacher.name LIKE %?1% OR s.teacher.email LIKE %?1%")
    Page<SubjectEntity> findAllByTeacherContaining(String teacher, Pageable pageable);
    List<SubjectEntity> findAllBySchedule_Id(String scheduleId);
    void deleteAllByTeacher(TeacherEntity teacher);
}
