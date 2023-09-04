package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.exception.TokenInvalidException;
import com.highschool.highschoolsystem.exception.UserNotFoundException;
import jakarta.servlet.http.Cookie;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface TeacherService extends GeneralService<TeacherEntity> {
    TeacherEntity save(TeacherEntity teacherEntity);
    TeacherEntity add(TeacherEntity teacherEntity);
    Optional<TeacherEntity> findByToken(String token) throws TokenInvalidException;
    TeacherEntity findById(String id) throws UserNotFoundException;
    void deleteById(String id);
    TeacherEntity updateById(String id, TeacherResponse teacher);
    Iterable<TeacherResponse> findAllTeachers();
    Iterable<TeacherResponse> findByName(String query);
    Optional<TeacherEntity> findByUsername(String username);
    String requireTeacherToken(Cookie token);
    long count();
}
