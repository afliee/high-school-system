package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public TeacherEntity save(TeacherEntity teacherEntity) {
        teacherEntity.setPassword(passwordEncoder.encode(teacherEntity.getPassword()));
        return teacherRepository.save(teacherEntity);
    }

    @Override
    public TeacherEntity update(TeacherEntity teacherEntity) {
        return null;
    }

    @Override
    public Optional<TeacherEntity> findById(String id) {
        return Optional.empty();
    }


    @Override
    public void delete(String id) {

    }

    @Override
    public Iterable<TeacherEntity> findAll() {
        return null;
    }

    @Override
    public TeacherEntity add(TeacherEntity teacherEntity) {
        return null;
    }
}
