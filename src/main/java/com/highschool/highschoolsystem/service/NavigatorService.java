package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.config.RegisterStatus;
import com.highschool.highschoolsystem.dto.request.DoCheckRequest;
import com.highschool.highschoolsystem.dto.request.NavigatorRegisterRequest;
import com.highschool.highschoolsystem.dto.response.NavigatorRegisterResponse;
import com.highschool.highschoolsystem.dto.response.StudentResponse;
import com.highschool.highschoolsystem.entity.NavigatorEntity;
import com.highschool.highschoolsystem.exception.NotFoundException;
import com.highschool.highschoolsystem.exception.UserExistException;
import com.highschool.highschoolsystem.repository.NavigatorRepository;
import com.highschool.highschoolsystem.repository.StudentRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class NavigatorService {
    @Autowired
    private NavigatorRepository navigatorRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private JwtService jwtService;

    public NavigatorRegisterResponse register(NavigatorRegisterRequest request) {
        navigatorRepository.findByStudent_Id(request.getId()).ifPresent(navigatorEntity -> {
            throw new UserExistException(navigatorEntity.getStudent().getFullName() + " student already is a navigator.");
        });

        var student = studentRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException("Student not found."));

        var navigator = NavigatorEntity.builder()
                .attendances(Collections.emptyList())
                .student(student)
                .createdAt(request.getCreatedAt())
                .status(RegisterStatus.SUBMITTED)
                .build();

        navigatorRepository.save(navigator);

        return NavigatorRegisterResponse.builder()
                .student(StudentResponse.builder()
                        .id(student.getId())
                        .fullName(student.getFullName())
                        .email(student.getEmail())
                        .build())
                .status(RegisterStatus.SUBMITTED.getValue())
                .message("Your request has been submitted. Please wait for approval.")
                .build();
    }

    public NavigatorEntity findNavigatorAlreadyRegistered(String token) {
        var username = jwtService.extractUsername(token);
        if (username == null) {
            throw new NotFoundException("User not found.");
        }

        return navigatorRepository.findByStudent_Name(username).orElse(null);
    }

    public Page<NavigatorRegisterResponse> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return navigatorRepository.findAll(pageRequest).map(this::mapToResponse);
    }

    private NavigatorRegisterResponse mapToResponse(NavigatorEntity navigatorEntity) {
        return NavigatorRegisterResponse.builder()
                .id(navigatorEntity.getId())
                .student(StudentResponse.builder()
                        .id(navigatorEntity.getStudent().getId())
                        .fullName(navigatorEntity.getStudent().getFullName())
                        .email(navigatorEntity.getStudent().getEmail())
                        .avatar(navigatorEntity.getStudent().getAvatar())
                        .name(navigatorEntity.getStudent().getName())
                        .build())
                .status(navigatorEntity.getStatus().getValue())
                .build();
    }

    public NavigatorRegisterResponse doCheck(DoCheckRequest doCheckRequest, String navigatorId) {
        var navigator = navigatorRepository.findById(doCheckRequest.getId()).orElseThrow(
                () -> new NotFoundException("Navigator not found.")
        );

        var status = RegisterStatus.get(doCheckRequest.getStatus());
        if (status == null) {
            throw new NotFoundException("Status not found.");
        }
        System.out.println("status = " + status);
        navigator.setStatus(status);
        navigatorRepository.save(navigator);

        return this.mapToResponse(navigator);
    }

    public NavigatorEntity getNavigator(String studentId) {
        return navigatorRepository.findByStudent_Id(studentId).orElse(null);
    }
}
