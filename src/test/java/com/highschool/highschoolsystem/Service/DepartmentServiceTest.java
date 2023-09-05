package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.DepartmentConverter;
import com.highschool.highschoolsystem.dto.request.DepartmentRequest;
import com.highschool.highschoolsystem.dto.response.DepartmentResponse;
import com.highschool.highschoolsystem.entity.DepartmentEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.repository.DepartmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DepartmentServiceTest {
    @Mock
    private DepartmentRepository departmentRepository;

    private DepartmentService departmentService;

    MockedStatic<DepartmentConverter> departmentConverterMockedStatic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        departmentConverterMockedStatic = mockStatic(DepartmentConverter.class);
        departmentService = new DepartmentService(departmentRepository);
    }

    @AfterEach
    void tearDown() {
        departmentConverterMockedStatic.close();
    }

    @Test
    void save_ValidAddDepartmentRequest_ShouldReturnTheDepartmentResponse() {
        DepartmentRequest departmentRequest = DepartmentRequest.builder().name("name").build();
        DepartmentEntity departmentEntity = new DepartmentEntity();
        when(departmentRepository.save(departmentEntity)).thenReturn(departmentEntity);
        when(departmentRepository.findByName(departmentRequest.getName())).thenReturn(Optional.empty());

        departmentConverterMockedStatic.when(() -> DepartmentConverter.toEntity(departmentRequest)).thenReturn(departmentEntity);
        departmentConverterMockedStatic.when(() -> DepartmentConverter.toResponse(departmentEntity)).thenReturn(new DepartmentResponse());
        DepartmentResponse departmentResponse = departmentService.save(departmentRequest);

        verify(departmentRepository).save(departmentEntity);
    }

    @Test
    void update_ValidUpdateDepartmentRequest_ShouldReturnTheDepartmentResponse() {
        DepartmentRequest departmentRequest = new DepartmentRequest();
        DepartmentEntity departmentEntity = DepartmentEntity.builder().foundedDate(LocalDate.now()).build();
        when(departmentRepository.save(departmentEntity)).thenReturn(departmentEntity);
        when(departmentRepository.findById(departmentEntity.getId())).thenReturn(Optional.of(departmentEntity));

        DepartmentResponse departmentResponse = departmentService.update(departmentEntity.getId(), departmentRequest);

        verify(departmentRepository, times(1)).findById(departmentEntity.getId());
        verify(departmentRepository, times(1)).save(departmentEntity);
    }

    @Test
    void findAll_ShouldReturnAllDepartments() {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        when(departmentRepository.findAll()).thenReturn(List.of(departmentEntity));

        Iterable<DepartmentEntity> departments = departmentService.findAll();

        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnDepartment() {
        DepartmentEntity departmentEntity = DepartmentEntity.builder().foundedDate(LocalDate.now()).teachers(List.of()).build();
        when(departmentRepository.findById("1")).thenReturn(Optional.of(departmentEntity));
        departmentConverterMockedStatic.when(() -> DepartmentConverter.toResponse(departmentEntity)).thenReturn(new DepartmentResponse());

        DepartmentResponse department = departmentService.findById("1");

        verify(departmentRepository, times(1)).findById("1");
    }

    @Test
    void save_WhenDepartmentAlreadyExist_ShouldThrowsRuntimeException() {
        DepartmentRequest departmentRequest = DepartmentRequest.builder().name("name").build();
        DepartmentEntity departmentEntity = new DepartmentEntity();
        when(departmentRepository.findByName(departmentRequest.getName())).thenReturn(Optional.of(departmentEntity));

        assertThrows(RuntimeException.class, () -> departmentService.save(departmentRequest));
    }

    @Test
    void update_WhenDepartmentNotFound_ShouldThrowNotFoundException() {
        DepartmentRequest departmentRequest = new DepartmentRequest();
        DepartmentEntity departmentEntity = DepartmentEntity.builder().foundedDate(LocalDate.now()).build();
        when(departmentRepository.findById(departmentEntity.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> departmentService.update(departmentEntity.getId(), departmentRequest));
    }

    @Test
    void findById_WhenDepartmentNotFound_ShouldThrowNotFoundException() {
        DepartmentEntity departmentEntity = DepartmentEntity.builder().foundedDate(LocalDate.now()).teachers(List.of()).build();
        when(departmentRepository.findById("1")).thenReturn(Optional.empty());
        departmentConverterMockedStatic.when(() -> DepartmentConverter.toResponse(departmentEntity)).thenReturn(new DepartmentResponse());

        assertThrows(RuntimeException.class, () -> departmentService.findById("1"));
    }

    @Test
    void save_WhenDepartmentAlreadyExist_ShouldThrowRunTimeException() {
        DepartmentRequest departmentRequest = DepartmentRequest.builder().name("name").build();
        DepartmentEntity departmentEntity = new DepartmentEntity();
        when(departmentRepository.findByName(departmentRequest.getName())).thenReturn(Optional.of(departmentEntity));

        assertThrows(RuntimeException.class, () -> departmentService.save(departmentRequest));
    }
}

