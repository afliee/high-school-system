package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.exception.TokenInvalidException;

import java.util.Optional;

public interface TeacherService extends GeneralService<TeacherEntity> {
    TeacherEntity save(TeacherEntity teacherEntity);
    TeacherEntity add(TeacherEntity teacherEntity);
    Optional<TeacherEntity> findByToken(String token) throws TokenInvalidException;
}
