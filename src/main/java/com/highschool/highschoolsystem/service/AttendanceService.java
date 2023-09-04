package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.AttendanceConverter;
import com.highschool.highschoolsystem.dto.request.AttendanceRequest;
import com.highschool.highschoolsystem.dto.response.AttendanceResponse;
import com.highschool.highschoolsystem.entity.AttendanceEntity;
import com.highschool.highschoolsystem.entity.BaseEntity;
import com.highschool.highschoolsystem.entity.NavigatorEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.AttendanceRepository;
import com.highschool.highschoolsystem.repository.NavigatorRepository;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceService {
    public static final String TAG = "AttendanceService";
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NavigatorService navigatorService;
    @Autowired
    private ClassService classService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private NavigatorRepository navigatorRepository;

    public Map<LocalDate, List<AttendanceEntity>> getHistory(String classId) {
        var attendances = attendanceRepository.findAllByClassEntity_Id(classId);

        return attendances.stream().collect(
                Collectors.groupingBy(BaseEntity::getCreatedDate,
                        Collectors.toList()
                )
        );
    }

    public String requireStudent(Cookie cookie) {
        if (cookie == null) {
            return "redirect:/?component=chooseLogin";
        }

        var username = jwtService.extractUsername(cookie.getValue());
        var user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return "redirect:/?component=chooseLogin";
        }

        return user.getUserId();
    }

    @Transactional
    public void submit(NavigatorEntity navigator, AttendanceRequest attendanceRequest) {
        var studentIds = attendanceRequest.getStudentIds();
        var classId = attendanceRequest.getClassId();

        var classEntity = classService.findById(classId);
        var students = studentIds.stream().map(studentId -> studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException("student not found"))).toList();
//        var studentAbsent = classEntity.getStudents().stream().filter(student -> !students.contains(student)).toList();
        System.out.println(studentIds);
        System.out.println("size: " + students.size());
        var attendance = AttendanceEntity.builder()
                .classEntity(classEntity)
                .students(students)
                .navigator(navigator)
                .present(students.size())
                .build();
        attendanceRepository.save(attendance);

//        students.forEach(student -> {
//            student.getAttendances().add(attendance);
//            studentRepository.save(student);
//        });
    }


    @Transactional
    public AttendanceEntity update(String id, AttendanceRequest request) {
        var attendance = attendanceRepository.findById(id).orElseThrow(() -> new NotFoundException("attendance not found"));

        var studentIds = request.getStudentIds();
        var classId = request.getClassId();

        var classEntity = classService.findById(classId);
        var students = studentIds.stream().map(studentId -> studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException("student not found"))).toList();

        attendance.setStudents(students);
        attendance.setPresent(students.size());

        System.out.println(TAG + " | update: " + attendance.getStudents().size());

//        System.out.println(TAG + " | success: " + attendance.getStudents().size());
        return attendance;

    }


    public AttendanceResponse getAttendance(String id) {
        return attendanceRepository.findById(id).map(AttendanceConverter::toResponse).orElseThrow(() -> new NotFoundException("attendance not found"));
    }
}
