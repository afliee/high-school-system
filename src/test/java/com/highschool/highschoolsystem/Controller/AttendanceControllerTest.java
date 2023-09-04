//package com.highschool.highschoolsystem.controller;
//
//import com.highschool.highschoolsystem.exception.NotFoundException;
//import com.highschool.highschoolsystem.repository.ClassRepository;
//import com.highschool.highschoolsystem.repository.SemesterRepository;
//import com.highschool.highschoolsystem.repository.StudentRepository;
//import com.highschool.highschoolsystem.repository.UserRepository;
//import com.highschool.highschoolsystem.service.AttendanceService;
//import com.highschool.highschoolsystem.service.JwtService;
//import com.highschool.highschoolsystem.service.NavigatorService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.ui.Model;
//import org.springframework.web.util.WebUtils;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class AttendanceControllerTest {
//
//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private StudentRepository studentRepository;
//
//    @Mock
//    private NavigatorService navigatorService;
//
//    @Mock
//    private SemesterRepository semesterRepository;
//
//    @Mock
//    private ClassRepository classRepository;
//
//    @Mock
//    private AttendanceService attendanceService;
//
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @Mock
//    private Model model;
//
//    @InjectMocks
//    private AttendanceController attendanceController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testIndex_RedirectToLoginPageWhenStudentNotFound() {
//        // Arrange
//        String token = "testToken";
//        Cookie cookie = new Cookie("token", token);
//        when(WebUtils.getCookie(request, "token")).thenReturn(cookie);
//        when(attendanceService.requireStudent(token)).thenReturn("redirect:/?component=chooseLogin");
//
//        // Act
//        String result = attendanceController.index(request, response, model);
//
//        // Assert
//        assertEquals("redirect:/?component=chooseLogin", result);
//    }
//
//    @Test
//    void testIndex_RedirectToChooseLoginPageWhenStudentIsNull() {
//        // Arrange
//        String token = "testToken";
//        Cookie cookie = new Cookie("token", token);
//        when(WebUtils.getCookie(request, "token")).thenReturn(cookie);
//        when(attendanceService.requireStudent(token)).thenReturn("studentId");
//        when(studentRepository.findById("studentId")).thenReturn(Optional.empty());
//
//        // Act
//        String result = attendanceController.index(request, response, model);
//
//        // Assert
//        assertEquals("redirect:/?component=chooseLogin", result);
//    }
//
//    @Test
//    void testIndex_ReturnsAttendanceIndexPage() {
//        // Arrange
//        String token = "testToken";
//        Cookie cookie = new Cookie("token", token);
//        when(WebUtils.getCookie(request, "token")).thenReturn(cookie);
//        when(attendanceService.requireStudent(token)).thenReturn("studentId");
//        when(studentRepository.findById("studentId")).thenReturn(Optional.of(new Student()));
//        when(navigatorService.getNavigator("studentId")).thenReturn(new Navigator());
//        when(semesterRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(any(), any()))
//                .thenReturn(Optional.empty());
//
//        // Act
//        String result = attendanceController.index(request, response, model);
//
//        // Assert
//        assertEquals("pages/attendance/index", result);
//        verify(model).addAttribute(eq("navigator"), any(Navigator.class));
//        verify(model).addAttribute(eq("classes"), any(List.class));
//        verify(model).addAttribute(eq("breadCrumbs"), any(List.class));
//        verify(model).addAttribute(eq("student"), any(Student.class));
//    }
//
//    @Test
//    void testAttendance_ThrowsNotFoundExceptionWhenClassNotFound() {
//        // Arrange
//        String classId = "testClassId";
//        String token = "testToken";
//        Cookie cookie = new Cookie("token", token);
//        when(WebUtils.getCookie(request, "token")).thenReturn(cookie);
//        when(attendanceService.requireStudent(token)).thenReturn("studentId");
//        when(studentRepository.findById("studentId")).thenReturn(Optional.of(new Student()));
//        when(navigatorService.getNavigator("studentId")).thenReturn(new Navigator());
//        when(classRepository.findById(classId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(NotFoundException.class, () -> attendanceController.attendance(classId, request, response, model));
//    }
//
//    @Test
//    void testAttendance_ReturnsAttendancePage() {
//        // Arrange
//        String classId = "testClassId";
//        String token = "testToken";
//        Cookie cookie = new Cookie("token", token);
//        when(WebUtils.getCookie(request, "token")).thenReturn(cookie);
//        when(attendanceService.requireStudent(token)).thenReturn("studentId");
//        when(studentRepository.findById("studentId")).thenReturn(Optional.of(new Student()));
//        when(navigatorService.getNavigator("studentId")).thenReturn(new Navigator());
//        ClassEntity classEntity = new ClassEntity();
//        when(classRepository.findById(classId)).thenReturn(Optional.of(classEntity));
//        List<AttendanceHistory> history = List.of(new AttendanceHistory());
//        when(attendanceService.getHistory(classId)).thenReturn(history);
//
//        // Act
//        String result = attendanceController.attendance(classId, request, response, model);
//
//        // Assert
//        assertEquals("pages/attendance/attendance", result);
//        verify(model).addAttribute(eq("navigator"), any(Navigator.class));
//        verify(model).addAttribute(eq("class"), eq(classEntity));
//        verify(model).addAttribute(eq("student"), any(Student.class));
//        verify(model).addAttribute(eq("history"), eq(history));
//        verify(model).addAttribute(eq("breadCrumbs"), any(List.class));
//    }
//}