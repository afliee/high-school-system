package com.highschool.highschoolsystem.controller.api;

import com.highschool.highschoolsystem.dto.request.AddClassRequest;
import com.highschool.highschoolsystem.dto.response.ClassResponse;
import com.highschool.highschoolsystem.entity.ClassEntity;
import com.highschool.highschoolsystem.service.ClassService;
import com.highschool.highschoolsystem.util.spreadsheet.ExcelUtil;
import org.apache.tomcat.util.http.parser.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ClassApiControllerTest {

    @Mock
    private ClassService classService;

    @InjectMocks
    private ClassApiController classApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void add_shouldCallClassServiceSaveMethodIfFileIsExcel() {
        AddClassRequest request = new AddClassRequest();
        MultipartFile students = createMockMultipartFile("students.xlsx");
        MockedStatic<ExcelUtil> excelUtilMockedStatic = mockStatic(ExcelUtil.class);

        request.setStudents(students);

        excelUtilMockedStatic.when(() -> ExcelUtil.isExcel(students)).thenReturn(true);

        ResponseEntity<?> response = classApiController.add(request);

        verify(classService).save(request);
    }

    @Test
    void add_shouldThrowExceptionIfFileIsNotExcel() {
        AddClassRequest request = new AddClassRequest();
        MultipartFile students = createMockMultipartFile("students.txt");
        MockedStatic<ExcelUtil> excelUtilMockedStatic = mockStatic(ExcelUtil.class);

        request.setStudents(students);

        excelUtilMockedStatic.when(() -> ExcelUtil.isExcel(students)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            classApiController.add(request);
        });

        verify(classService, never()).save(any());
    }


    @Test
    void get_withId_shouldReturnClassEntity() {
        String id = "classId";
        int page = 0;
        int size = 8;
        ClassEntity classEntity = new ClassEntity();

        when(classService.get(id, page, size)).thenReturn(ClassResponse.builder().build());

        ResponseEntity<?> response = classApiController.get(id, page, size);

        verify(classService).get(id, page, size);
    }

    @Test
    void get_withoutId_shouldReturnPageOfClasses() {
        int page = 0;
        int size = 10;
        String semesterId = "current";
        String levelId = "";
        Page<?> classPage = Page.empty();


        ResponseEntity<Page<?>> response = classApiController.get(page, size, semesterId, levelId);

        verify(classService).get(page, size, semesterId);
    }


    @Test
    void delete_withIdAndStudentId_shouldCallClassServiceDeleteMethodWithStudentId() {
        String id = "classId";
        String studentId = "studentId";

        ResponseEntity<?> response = classApiController.delete(id, studentId);

        verify(classService).delete(id, studentId);
        assert response.getStatusCode() == HttpStatus.OK;
    }

    @Test
    void delete_withClassIdOnly_shouldCallClassServiceDeleteMethod() {
        String classId = "classId";

        ResponseEntity<?> response = classApiController.delete(classId);

        verify(classService).delete(classId);
        assert response.getStatusCode() == HttpStatus.OK;
    }
    @Test
    void setChairman_shouldCallClassServiceSetChairmanMethod() {
        String classId = "classId";
        String teacherId = "teacherId";

        ResponseEntity<?> response = classApiController.setChairman(classId, teacherId);

        verify(classService).setChairman(classId, teacherId);
    }

    // Helper method to create a mock MultipartFile
    private MultipartFile createMockMultipartFile (String filename) {
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        when(mockMultipartFile.getOriginalFilename()).thenReturn(filename);
        return mockMultipartFile;
    }
}