package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.converter.DepartmentConverter;
import com.highschool.highschoolsystem.dto.request.DepartmentRequest;
import com.highschool.highschoolsystem.dto.response.DepartmentResponse;
import com.highschool.highschoolsystem.entity.DepartmentEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DepartmentServiceTest {
    @Mock
    private DepartmentRepository departmentRepository;

    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        departmentService = new DepartmentService(departmentRepository);
    }

    @Test
    void save_ValidAddDepartmentRequest_ShouldReturnTheDepartmentResponse() {
        DepartmentRequest departmentRequest = DepartmentRequest.builder().name("name").build();
        DepartmentEntity departmentEntity = new DepartmentEntity();
        when(departmentRepository.save(departmentEntity)).thenReturn(departmentEntity);
        when(departmentRepository.findByName(departmentRequest.getName())).thenReturn(Optional.empty());
        MockedStatic<DepartmentConverter> departmentConverterMockedStatic = mockStatic(DepartmentConverter.class);

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
        MockedStatic<DepartmentConverter> departmentConverterMockedStatic = mockStatic(DepartmentConverter.class);
        when(departmentRepository.findById("1")).thenReturn(Optional.of(departmentEntity));
        departmentConverterMockedStatic.when(() -> DepartmentConverter.toResponse(departmentEntity)).thenReturn(new DepartmentResponse());

        DepartmentResponse department = departmentService.findById("1");

        verify(departmentRepository, times(1)).findById("1");
    }
}
