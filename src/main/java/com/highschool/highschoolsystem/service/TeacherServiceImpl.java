package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.TeacherConverter;
import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.exception.TokenInvalidException;
import com.highschool.highschoolsystem.exception.UserNotFoundException;
import com.highschool.highschoolsystem.repository.TeacherRepository;
import com.highschool.highschoolsystem.repository.TokenRepository;
import com.highschool.highschoolsystem.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

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
    public TeacherEntity findById(String id) {
        return teacherRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
    }

    @Override
    public void deleteById(String id) {
        String userId = userRepository.findByUserId(id).orElseThrow(
                () -> new UserNotFoundException("User not found")
        ).getId();

        tokenRepository.deleteByUserId(userId);
        userRepository.deleteByUserId(id);
        teacherRepository.deleteById(id);
    }

    @Override
    public TeacherEntity updateById(String id, TeacherResponse teacher) {
//        save teacherEntity where id = id
        var teacherEntity = teacherRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        TeacherConverter.to(teacher, teacherEntity);
        return teacherRepository.save(teacherEntity);
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
