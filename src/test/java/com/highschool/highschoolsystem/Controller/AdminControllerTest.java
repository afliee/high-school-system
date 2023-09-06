package com.highschool.highschoolsystem.controller;

import com.highschool.highschoolsystem.controller.AdminController;
import com.highschool.highschoolsystem.dto.response.LevelResponse;
import com.highschool.highschoolsystem.dto.response.SemesterResponse;
import com.highschool.highschoolsystem.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private AdminService adminService;
    @Mock
    private DepartmentService departmentService;
    @Mock
    private SubjectService subjectService;
    @Mock
    private SemesterService semesterService;
    @Mock
    private DayService dayService;
    @Mock
    private ShiftService shiftService;
    @Mock
    private LevelService levelService;
    @Mock
    private LessonService lessonService;
    @Mock
    private StatisticService statisticService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Model model;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void index_shouldRedirectWhenNotLoggedIn() {
        // Arrange
        when(adminService.requireAdminLogin(request)).thenReturn("redirect:/login");

        // Act
        String viewName = adminController.index(request, response, model);

        // Assert
        assertEquals("redirect:/login", viewName);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void login_ShouldReturnUrlToLoginPage() {
        assertEquals("pages/auth/adminSignin", adminController.login(request, response));
    }

    @Test
    void getMembers_ShouldReturnUrlToTeacherPage() {
        assertEquals("pages/admin/tests", adminController.teachers(request, response, "test", 1, model));
    }

    @Test
    void getClassesShouldReturnUrlToClassPage() {
        assertEquals("pages/admin/classes", adminController.classes(request, response));
    }

    @Test
    void getClassDetails_ShouldReturnClassDetailUrl() {
        assertEquals("pages/admin/classDetails", adminController.classDetails(request, "test", model));
    }

    @Test
    void getUpdateSubject_ShouldReturnUpdateSubjectUrl() {
        assertEquals("pages/admin/subject/update_subject", adminController.subject(request, "test", model));
    }

    @Test
    void getSubjects_ShouldReturnSubjectsPageUrl() {
        assertEquals("pages/admin/subjects", adminController.subjects(request, response, model));
    }

    @Test
    void getAddSubject_ShouldReturnAddSubjectUrl() {
        assertEquals("pages/admin/addSubject", adminController.addSubject("1", request, model));
    }

    @Test
    void getSubjectDetails_ShouldReturnSubjectDetailsUrl() {
        assertEquals("pages/admin/subject/subject_details", adminController.subjectDetail("1", request, model));
    }

    @Test
    void getSemesters_ShouldReturnSemestersUrl() {
        assertEquals("pages/admin/semester", adminController.showSemester(request, response, model));
    }

    @Test
    void getDepartmentDetails_ShouldReturnDepartmentDetailsUrl() {
        assertEquals("pages/admin/departmentDetails", adminController.departmentDetails("1", request, model));
    }

    @Test
    void getRangeScheduling_ShouldReturnRangeSchedulingUrl() {
        assertEquals("pages/admin/schedule/rangeScheduling", adminController.rangeScheduling(request, response, model));
    }

    @Test
    void getReviewScheduling_ShouldReturnUrlToReviewSchedulingPage() {
        assertEquals("pages/admin/schedule/reviewScheduling", adminController.reviewScheduling(request, response, model));
    }

    @Test
    void getScheduleDetail_ShouldReturnUrlToScheduleDetailPage() {
        assertEquals("pages/admin/schedule/ScheduleDetail", adminController.scheduleDetail("1", "1", request, model));
    }

    @Test
    void getLevel_ShouldReturnUrlToLevelPage() {
        assertEquals("pages/admin/level", adminController.levelShow(request, response, model));
    }

    @Test
    void getLevelDetail_ShouldReturnUrlToLevelDetailPage() {
        assertEquals("pages/admin/levelDetails", adminController.levelDetail("1", request, model));
    }

    @Test
    void getSemester_ShouldReturnUrlToSemesterPage() {
        assertEquals("pages/admin/semester", adminController.showSemester(request, response, model));
    }

    @Test
    void getSemesterCreate_ShouldReturnUrlToSemesterCreatePage() {
        assertEquals("pages/admin/semester/createSemester", adminController.createSemester(request, response, model));
    }

    @Test
    void getSemesterUpdatePage_ShouldReturnUrlToSemesterUpdatePage() {
        assertEquals("pages/admin/semester/updateSemester", adminController.updateSemester("1", request, response, model));
    }
}