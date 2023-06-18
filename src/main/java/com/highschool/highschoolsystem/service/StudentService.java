package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.entity.StudentEntity;

public interface StudentService extends GeneralService<StudentEntity> {
    StudentEntity save(StudentEntity student);

    StudentEntity add(StudentEntity student);

    void delete(StudentEntity student);
}
