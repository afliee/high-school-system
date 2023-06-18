package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.entity.TeacherEntity;

public interface TeacherService extends GeneralService<TeacherEntity> {
    TeacherEntity save(TeacherEntity teacherEntity);
    TeacherEntity add(TeacherEntity teacherEntity);
}
