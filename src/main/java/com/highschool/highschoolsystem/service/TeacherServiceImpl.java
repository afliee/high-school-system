package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.exception.TokenInvalidException;
import com.highschool.highschoolsystem.repository.TeacherRepository;
import com.highschool.highschoolsystem.repository.TokenRepository;
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

    @Autowired
    private TokenRepository tokenRepository;

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

    @Override
    public Optional<TeacherEntity> findByToken(String token)  throws TokenInvalidException {
        var tokenEntity = tokenRepository.findByToken(token).orElseThrow(
                () -> new TokenInvalidException("Token not found")
        );

         var teacher = teacherRepository.findById(tokenEntity.getUser().getUserId()).orElseThrow(
                () -> new TokenInvalidException("User not found")
        );

         return Optional.of(teacher);
    }
}
