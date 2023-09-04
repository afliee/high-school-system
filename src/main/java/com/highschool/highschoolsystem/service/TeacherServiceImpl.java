package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.TeacherConverter;
import com.highschool.highschoolsystem.dto.response.TeacherResponse;
import com.highschool.highschoolsystem.entity.BaseEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.exception.TokenInvalidException;
import com.highschool.highschoolsystem.exception.UserNotFoundException;
import com.highschool.highschoolsystem.repository.*;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//@RequiredArgsConstructor
@Transactional
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private LessonRepository lessonRepository;

//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

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

        var teacher = teacherRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        classRepository.findByChairmanId(teacher.getId()).ifPresent(aClass -> {
            aClass.setChairman(null);
            classRepository.save(aClass);
        });

        lessonRepository.deleteAllBySubjectIdIn(teacher.getSubjects().stream().map(BaseEntity::getId).toList());

        subjectRepository.deleteAllByTeacher(teacher);
        tokenRepository.deleteByUserId(userId).orElse(null);
        userRepository.deleteByUserId(id);
        teacherRepository.deleteById(id);
    }

    @Override
    public TeacherEntity updateById(String id, TeacherResponse teacher) {
//        save teacherEntity where id = id
        var teacherEntity = teacherRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        if (teacher.getDepartmentId() != null) {
            var department = departmentRepository.findById(teacher.getDepartmentId()).orElseThrow(
                    () -> new UserNotFoundException("Department not found")
            );
            teacherEntity.setDepartment(department);
        }

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
    public Iterable<TeacherResponse> findAllTeachers() {
        var teachers =  teacherRepository.findAll().stream().map(teacher ->
                TeacherResponse.builder()
                        .id(teacher.getId())
                        .fullName(teacher.getFullName())
                        .build()
        );
//        return list to iterable
//        filter attributes != null

        return teachers.toList();
    }

    @Override
    public Iterable<TeacherResponse> findByName(String query) {
        return teacherRepository.findByFullNameContainingIgnoreCase(query).stream().map(teacher ->
                TeacherResponse.builder()
                        .id(teacher.getId())
                        .fullName(teacher.getFullName())
                        .build()
        ).toList();
    }

    @Override
    public Optional<TeacherEntity> findByUsername(String username) {
        return teacherRepository.findByName(username);
    }

    @Override
    public String requireTeacherToken(Cookie tokenCookie) {
        if (tokenCookie == null || tokenCookie.getValue().isEmpty()) {
            return "redirect:/login?with=teacher";
        }

        if (!jwtService.validateToken(tokenCookie.getValue())) {
            return "redirect:/login?with=teacher";
        }

        if (!jwtService.isTeacher(tokenCookie.getValue())) {
            return "redirect:/login?with=teacher";
        }
        return null;
    }

    @Override
    public long count() {
        return teacherRepository.count();
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
