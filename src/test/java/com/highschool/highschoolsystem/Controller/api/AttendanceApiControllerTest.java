package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.AttendanceRequest;
import com.highschool.highschoolsystem.dto.response.AttendanceResponse;
import com.highschool.highschoolsystem.entity.NavigatorEntity;
import com.highschool.highschoolsystem.service.AttendanceService;
import com.highschool.highschoolsystem.service.NavigatorService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.WebUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AttendanceApiControllerTest {

    @Mock
    private AttendanceService attendanceService;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AttendanceApiController attendanceApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submit_shouldCallAttendanceServiceSubmitMethod() {
        AttendanceRequest attendanceRequest = new AttendanceRequest();
        Cookie cookie = new Cookie("token", "testToken");

        when(request.getHeader("Cookie")).thenReturn("token=testToken");
        when(WebUtils.getCookie(request, "token")).thenReturn(cookie);

        attendanceApiController.submit(attendanceRequest, request);

        verify(attendanceService).submit(any(), eq(attendanceRequest));
    }

    @Test
    void submit_shouldReturnOkIfRequireStudentReturnsValidNavigator() {
        AttendanceRequest attendanceRequest = new AttendanceRequest();
        Cookie cookie = new Cookie("token", "testToken");
        NavigatorEntity navigator = NavigatorEntity.builder().build();

        when(request.getHeader("Cookie")).thenReturn("token=testToken");
        when(WebUtils.getCookie(request, "token")).thenReturn(cookie);
        when(attendanceService.requireStudent(cookie)).thenReturn("validNavigator");
        when(navigatorService.getNavigator("validNavigator")).thenReturn(navigator);

        ResponseEntity<?> response = attendanceApiController.submit(attendanceRequest, request);

        verify(attendanceService, times(1)).submit(navigator, attendanceRequest);
    }

    @Test
    void submit_shouldReturnBadRequestIfRequireStudentReturnsRedirect() {
        AttendanceRequest attendanceRequest = new AttendanceRequest();
        Cookie cookie = new Cookie("token", "testToken");

        when(request.getHeader("Cookie")).thenReturn("token=testToken");
        when(WebUtils.getCookie(request, "token")).thenReturn(cookie);
        when(attendanceService.requireStudent(cookie)).thenReturn("redirect:/login");

        ResponseEntity<?> response = attendanceApiController.submit(attendanceRequest, request);

        verify(attendanceService, never()).submit(any(), any());
        assert response.getStatusCodeValue() == 400;
        assert response.getBody().equals("redirect:/login");
    }

    @Test
    void index_shouldCallAttendanceServiceGetAttendanceMethod() {
        String id = "attendanceId";
        Cookie cookie = new Cookie("token", "testToken");

        when(request.getHeader("Cookie")).thenReturn("token=testToken");
        when(WebUtils.getCookie(request, "token")).thenReturn(cookie);

        attendanceApiController.index(id, request);

        verify(attendanceService).getAttendance(id);
    }

    @Test
    void index_shouldReturnOkIfRequireStudentReturnsValidNavigator() {
        String id = "attendanceId";
        Cookie cookie = new Cookie("token", "testToken");
        AttendanceResponse attendanceResponse = AttendanceResponse.builder().build();

        when(request.getHeader("Cookie")).thenReturn("token=testToken");
        when(WebUtils.getCookie(request, "token")).thenReturn(cookie);
        when(attendanceService.requireStudent(cookie)).thenReturn("validNavigator");
        when(attendanceService.getAttendance(id)).thenReturn(attendanceResponse);

        ResponseEntity<?> response = attendanceApiController.index(id, request);

        assert response.getStatusCodeValue() == 200;
        assert response.getBody().equals("attendanceResult");
    }

    @Test
    void index_shouldReturnBadRequestIfRequireStudentReturnsRedirect() {
        String id = "attendanceId";
        Cookie cookie = new Cookie("token", "testToken");

        when(request.getHeader("Cookie")).thenReturn("token=testToken");
        when(WebUtils.getCookie(request, "token")).thenReturn(cookie);
        when(attendanceService.requireStudent(cookie)).thenReturn("redirect:/login");

        ResponseEntity<?> response = attendanceApiController.index(id, request);

        assert response.getStatusCodeValue() == 400;
        assert response.getBody().equals("redirect:/login");
    }
}