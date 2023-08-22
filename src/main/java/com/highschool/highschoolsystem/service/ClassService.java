package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.ClassConverter;
import com.highschool.highschoolsystem.converter.StudentConverter;
import com.highschool.highschoolsystem.converter.UserConverter;
import com.highschool.highschoolsystem.dto.request.AddClassRequest;
import com.highschool.highschoolsystem.dto.response.ClassResponse;
import com.highschool.highschoolsystem.entity.*;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.*;
import com.highschool.highschoolsystem.util.spreadsheet.ExcelUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ClassService {
    private static final Logger logger = Logger.getLogger(ClassService.class.getName());
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_LIMIT_GET_STUDENT = 3;
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
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private ScheduleRepository scheduleRepository;

    public ClassResponse save(AddClassRequest request) {
        try {
            MultipartFile students = request.getStudents();

            LevelEntity levelEntity = levelRepository.findById(request.getLevelId()).orElseThrow(
                    () -> new RuntimeException("Level not found")
            );

            SemesterEntity semesterEntity = semesterRepository.findById(request.getSemesterId()).orElseThrow(
                    () -> new RuntimeException("Semester not found")
            );

            var chairman = teacherRepository.findById(request.getChairman()).orElseThrow(
                    () -> new RuntimeException("Teacher not found")
            );

            List<StudentEntity> studentEntities = ExcelUtil.extractStudents(students.getInputStream(), passwordEncoder);
            var classEntity = ClassEntity.builder()
                    .name(request.getName())
                    .level(levelEntity)
                    .semester(semesterEntity)
                    .students(studentEntities)
                    .chairman(chairman)
                    .present(studentEntities.toArray().length)
                    .build();
            classRepository.save(classEntity);

//            set class for students
            for (StudentEntity studentEntity : studentEntities) {
                studentEntity.setClassEntity(classEntity);
                studentRepository.save(studentEntity);
                userRepository.save(UserConverter.toEntity(studentEntity));
            }
            levelEntity.getClasses().add(classEntity);
            levelRepository.save(levelEntity);
            return ClassConverter.toResponse(classEntity);
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

    public ClassResponse get(String id, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size).withSort(Sort.by("name").ascending());
        var classEntity = classRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Class not found")
        );
//        get students of class and convert to page
//        slice student with size
        var students = classEntity.getStudents().stream().skip((long) page * size).limit(size).toList();

        var content = students.stream().map(StudentConverter::toResponse).toList();

        var studentPage = new PageImpl<>(content, pageRequest, classEntity.getStudents().toArray().length);

        return ClassConverter.toResponse(classEntity, content, studentPage);
    }

    public Page<?> get(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size).withSort(Sort.by("name").ascending());
        var classes = classRepository.findAll(pageRequest);
//        convert content of response
//        change content to class response
        var content = classes.getContent().stream().map(classEntity -> ClassConverter.toResponse(classEntity, DEFAULT_LIMIT_GET_STUDENT)).toList();
        return new PageImpl<>(content, pageRequest, classes.getTotalElements());
    }

    public Page<?> get(int page, int size, String semesterId) {
        var pageRequest = PageRequest.of(page, size).withSort(Sort.by("name").ascending());

        if (semesterId.equals("current")) {
            var currentTime = LocalDate.now();

            System.out.println(currentTime);
//            System.out.println(endDate);
            var semester = semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(currentTime, currentTime).orElseThrow(
                    () -> new NotFoundException("Semester not found")
            );

            semesterId = semester.getId();

            var classes = classRepository.findAllBySemesterId(semesterId, pageRequest);
            var content = classes.getContent().stream().map(classEntity -> ClassConverter.toResponse(classEntity, DEFAULT_LIMIT_GET_STUDENT)).toList();
            return new PageImpl<>(content, pageRequest, classes.getTotalElements());
        } else {
            var classes = classRepository.findAllBySemesterId(semesterId, pageRequest);
            var content = classes.getContent().stream().map(classEntity -> ClassConverter.toResponse(classEntity, DEFAULT_LIMIT_GET_STUDENT)).toList();
            return new PageImpl<>(content, pageRequest, classes.getTotalElements());
        }
    }

    public Page<ClassResponse> get(int page, int size, String semesterId, String levelId) {
        var pageRequest = PageRequest.of(page, size).withSort(Sort.by("name").ascending());

        if (semesterId.equals("current")) {
            var currentTime = LocalDate.now();

            System.out.println(currentTime);
//            System.out.println(endDate);
            var semester = semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(currentTime, currentTime).orElseThrow(
                    () -> new NotFoundException("Semester not found")
            );

            semesterId = semester.getId();

            var classes = classRepository.findAllBySemesterIdAndLevelId(semesterId, levelId, pageRequest);
            var content = classes.getContent().stream().map(classEntity -> ClassConverter.toResponse(classEntity, DEFAULT_LIMIT_GET_STUDENT)).toList();
            return new PageImpl<>(content, pageRequest, classes.getTotalElements());
        } else {
            var classes = classRepository.findAllBySemesterIdAndLevelId(semesterId, levelId, pageRequest);
            var content = classes.getContent().stream().map(classEntity -> ClassConverter.toResponse(classEntity, DEFAULT_LIMIT_GET_STUDENT)).toList();
            return new PageImpl<>(content, pageRequest, classes.getTotalElements());
        }
    }

    @Transactional
    public void delete(String id, String studentId) {
        var user = userRepository.findByUserId(studentId).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        var token = tokenRepository.findByUserId(user.getId()).orElse(null);
        if (token != null) {
            tokenRepository.delete(token);
        }
        userRepository.deleteByUserId(studentId);
        studentRepository.deleteById(studentId);
        var classEntity = classRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Class not found")
        );

        classEntity.getStudents().removeIf(studentEntity -> studentEntity.getId().equals(studentId));
        classEntity.setPresent(classEntity.getPresent() - 1);
        classRepository.save(classEntity);
    }

    @Transactional
    public void delete(String classId) {
        var classEntity = classRepository.findById(classId).orElseThrow(
                () -> new NotFoundException("Class not found")
        );

        var students = classEntity.getStudents();
        for (StudentEntity student : students) {
            logger.info(student.getId() + " " + student.getName());
            var user = userRepository.findByUserId(student.getId()).orElseThrow(
                    () -> new NotFoundException("User not found")
            );

            var token = tokenRepository.findByUserId(user.getId()).orElse(null);
            if (token != null) {
                tokenRepository.delete(token);
            }
            userRepository.deleteByUserId(student.getId());
            studentRepository.deleteById(student.getId());
        }

        var schedule = classEntity.getSchedule();
        if (schedule != null) {
            var lessons = schedule.getLessons();


            scheduleRepository.delete(schedule);
        }
        classRepository.deleteById(classId);
    }

    public void setChairman(String classId, String teacherId) {
        var classEntity = classRepository.findById(classId).orElseThrow(
                () -> new NotFoundException("Class not found")
        );

        var isChairman = classRepository.findByChairmanId(teacherId).orElse(null);
        if (isChairman != null && !isChairman.getId().equals(classId)) {
            throw new RuntimeException("Teacher already chairman of other class");
        }

        var chairman = teacherRepository.findById(teacherId).orElseThrow(
                () -> new NotFoundException("Teacher not found")
        );

        classEntity.setChairman(chairman);
        classRepository.save(classEntity);
    }

    public ClassEntity findById(String id) {
        return classRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Class not found")
        );
    }

    public ClassEntity findByTeacher(String teacherId) {
        return classRepository.findByChairmanId(teacherId).orElseThrow(
                () -> new NotFoundException("Class not found")
        );
    }
}
