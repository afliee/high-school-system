package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.UserConverter;
import com.highschool.highschoolsystem.dto.request.AddClassRequest;
import com.highschool.highschoolsystem.entity.ClassEntity;
import com.highschool.highschoolsystem.entity.LevelEntity;
import com.highschool.highschoolsystem.entity.SemesterEntity;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.*;
import com.highschool.highschoolsystem.util.spreadsheet.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class ClassService {
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ClassEntity save(AddClassRequest request) {
        try {
            MultipartFile students = request.getStudents();

            LevelEntity levelEntity = levelRepository.findById(request.getLevelId()).orElseThrow(
                    () -> new RuntimeException("Level not found")
            );

            SemesterEntity semesterEntity = semesterRepository.findById(request.getSemesterId()).orElseThrow(
                    () -> new RuntimeException("Semester not found")
            );

            List<StudentEntity> studentEntities = ExcelUtil.extractStudents(students.getInputStream(), passwordEncoder);
            var classEntity = ClassEntity.builder()
                    .name(request.getName())
                    .level(levelEntity)
                    .semester(semesterEntity)
                    .students(studentEntities)
                    .present(studentEntities.toArray().length)
                    .build();
            classRepository.save(classEntity);

//            set class for students
            for (StudentEntity studentEntity : studentEntities) {
                studentEntity.setClassEntity(classEntity);
                studentRepository.save(studentEntity);
                userRepository.save(UserConverter.toEntity(studentEntity));
            }

            return classEntity;
        } catch (Exception e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream download(String classId) {
        try {
            var classEntity = classRepository.findById(classId).orElseThrow(
                    () -> new NotFoundException("Class not found")
            );

            return ExcelUtil.toExcel(classEntity.getStudents().stream().toList());
        } catch (Exception e) {
            throw new RuntimeException("fail to download excel file: " + e.getMessage());
        }
    }
}
