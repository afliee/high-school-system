package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.dto.request.UpdateStudentRequest;
import com.highschool.highschoolsystem.dto.response.StudentResponse;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentService(studentRepository);
    }

    @Test
    public void findOne_ShouldReturnStudentEntity() {
        StudentEntity studentEntity = new StudentEntity();
        when(studentRepository.findById(studentEntity.getId())).thenReturn(Optional.of(studentEntity));

        StudentEntity student = studentService.findOne(studentEntity.getId());

        assertEquals(studentEntity, student);
        verify(studentRepository, times(1)).findById(studentEntity.getId());
    }

    @Test
    public void update_ShouldReturnStudentResponse() {
        UpdateStudentRequest updateStudentRequest = new UpdateStudentRequest();
        StudentEntity studentEntity = new StudentEntity();
        when(studentRepository.save(studentEntity)).thenReturn(studentEntity);
        when(studentRepository.findById("1")).thenReturn(Optional.of(studentEntity));

        StudentResponse student = studentService.update("1", updateStudentRequest);

        assertEquals(studentEntity.getId(), student.getId());
        verify(studentRepository, times(1)).save(studentEntity);
    }

    @Test
    void get_ShouldReturnStudentResponse() {
        StudentEntity studentEntity = new StudentEntity();
        when(studentRepository.findById("1")).thenReturn(Optional.of(studentEntity));

        StudentResponse student = studentService.get("1");

        assertEquals(studentEntity.getId(), student.getId());
        verify(studentRepository, times(1)).findById("1");
    }

    @Test
    void testGet_StudentNotExists_ThrowsNotFoundException() {
        // Arrange
        String id = "1";
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> studentService.get(id));
    }

    @Test
    void testUpdate_StudentNotExists_ThrowsNotFoundException() {
        // Arrange
        String id = "1";
        UpdateStudentRequest request = new UpdateStudentRequest();
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> studentService.update(id, request));
    }

    @Test
    void get_WhenStudentNotFound_ShouldThrowNotFoundException() {
        when(studentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> studentService.get("1"));
    }

    @Test
    void getSubmittingTodayByStudent_WhenStudentNotFound_ShouldThrowNotFoundException() {
        when(studentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> studentService.getSubmittingTodayByStudent("1", "1"));
    }

    @Test
    void findOne_WhenStudentNotFound_ShouldThrowNotFoundException() {
        when(studentRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> studentService.findOne("1"));
    }
}

