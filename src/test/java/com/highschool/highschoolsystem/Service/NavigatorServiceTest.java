package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.config.RegisterStatus;
import com.highschool.highschoolsystem.dto.request.DoCheckRequest;
import com.highschool.highschoolsystem.dto.request.NavigatorRegisterRequest;
import com.highschool.highschoolsystem.dto.response.NavigatorRegisterResponse;
import com.highschool.highschoolsystem.entity.NavigatorEntity;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.exception.UserExistException;
import com.highschool.highschoolsystem.repository.NavigatorRepository;
import com.highschool.highschoolsystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class NavigatorServiceTest {
   @Mock
   private NavigatorRepository navigatorRepository;

   @Mock
    private StudentRepository studentRepository;

   @Mock
   private JwtService jwtService;

    private NavigatorService navigatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        navigatorService = new NavigatorService(navigatorRepository, studentRepository, jwtService);
    }

    @Test
    void getNavigator_ShouldReturnNavigator() {
        NavigatorEntity navigatorEntity = NavigatorEntity.builder().build();
        when(navigatorRepository.findByStudent_Id("1")).thenReturn(java.util.Optional.of(navigatorEntity));

        NavigatorEntity navigatorResponse = navigatorService.getNavigator("1");

        assertEquals(navigatorEntity.getId(), navigatorResponse.getId());
        verify(navigatorRepository, times(1)).findByStudent_Id("1");
    }

    @Test
    void register_ShouldReturnNavigatorRegisterResponse() {
        NavigatorRegisterRequest navigatorRegisterRequest = NavigatorRegisterRequest.builder().build();
        StudentEntity studentEntity = StudentEntity.builder().build();
        NavigatorEntity navigatorEntity = NavigatorEntity.builder().attendances(Collections.emptyList()).student(studentEntity).createdAt(navigatorRegisterRequest.getCreatedAt()).status(RegisterStatus.SUBMITTED).build();

        when(navigatorRepository.save(navigatorEntity)).thenReturn(navigatorEntity);
        when(studentRepository.findById(navigatorRegisterRequest.getId())).thenReturn(java.util.Optional.of(studentEntity));
        when(navigatorRepository.findByStudent_Id(navigatorRegisterRequest.getId())).thenReturn(java.util.Optional.empty());

        NavigatorRegisterResponse navigatorRegisterResponse = navigatorService.register(navigatorRegisterRequest);

        verify(navigatorRepository, times(1)).findByStudent_Id(navigatorRegisterRequest.getId());
        verify(navigatorRepository, times(1)).save(navigatorEntity);
        verify(studentRepository, times(1)).findById(navigatorRegisterRequest.getId());
    }

    @Test
    void findNavigatorAlreadyRegistered_ShouldReturnNavigator() {
        NavigatorEntity navigatorEntity = NavigatorEntity.builder().build();
        when(navigatorRepository.findByStudent_Name("1")).thenReturn(java.util.Optional.of(navigatorEntity));
        when(jwtService.extractUsername("1")).thenReturn("1");

        NavigatorEntity navigatorResponse = navigatorService.findNavigatorAlreadyRegistered("1");

        verify(navigatorRepository, times(1)).findByStudent_Name("1");
    }

    @Test
    void findAll_ShouldReturnAllNavigators() {
        NavigatorEntity navigatorEntity = NavigatorEntity.builder().build();
        PageRequest pageRequest = PageRequest.of(1, 1);
        when(navigatorRepository.findAll(pageRequest)).thenReturn(Page.empty());

        Page<NavigatorRegisterResponse> navigators = navigatorService.findAll(1, 1);

        verify(navigatorRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void register_WhenNavigatorAlreadyExists_ShouldThrowException() {
        NavigatorRegisterRequest navigatorRegisterRequest = NavigatorRegisterRequest.builder().build();
        NavigatorEntity navigatorEntity = NavigatorEntity.builder().student(StudentEntity.builder().fullName("test").build()).build();
        when(navigatorRepository.findByStudent_Id(navigatorRegisterRequest.getId())).thenReturn(java.util.Optional.of(navigatorEntity));

        assertThrows(UserExistException.class, () -> navigatorService.register(navigatorRegisterRequest));
    }

    @Test
    void register_WhenStudentNotFound_ShouldThrowException() {
        NavigatorRegisterRequest navigatorRegisterRequest = NavigatorRegisterRequest.builder().build();
        when(studentRepository.findById(navigatorRegisterRequest.getId())).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> navigatorService.register(navigatorRegisterRequest));
    }

    @Test
    void findNavigatorAlreadyRegistered_WhenUserNotFound_ShouldThrowException() {
        when(jwtService.extractUsername("1")).thenReturn(null);

        assertThrows(NotFoundException.class, () -> navigatorService.findNavigatorAlreadyRegistered("1"));
    }

    @Test
    void doCheck_WhenNavigatorNotFound_ShouldThrowNotFoundException() {
        DoCheckRequest doCheckRequest = DoCheckRequest.builder().build();
        when(navigatorRepository.findById("1")).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> navigatorService.doCheck(doCheckRequest, "1"));
    }


}
