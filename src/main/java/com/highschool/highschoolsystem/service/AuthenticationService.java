package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.auth.AuthenticationRequest;
import com.highschool.highschoolsystem.auth.AuthenticationResponse;
import com.highschool.highschoolsystem.auth.RegistrationRequest;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.TeacherRepository;
import com.highschool.highschoolsystem.util.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        switch (request.getRole()) {
            case "teacher": {
                TeacherEntity teacher = teacherRepository.findByName(request.getUsername()).orElseThrow();
                UserPrincipal userPrincipal = new UserPrincipal(teacher);
                String token = jwtService.generateToken(userPrincipal);
                return AuthenticationResponse.builder()
                        .token(token)
                        .tokenType("Bearer ")
                        .build();
            }
        }
        return null;
    }

    public AuthenticationResponse register(RegistrationRequest request, String role) {
        switch (role) {
            case "teacher": {
                TeacherEntity teacher = TeacherEntity.builder()
                        .name(request.getUsername())
                        .fullName(request.getFullName())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build();

                UserPrincipal userPrincipal = new UserPrincipal(teacher);
                teacherRepository.save(teacher);
                String token = jwtService.generateToken(userPrincipal);
                return AuthenticationResponse.builder()
                        .token(token)
                        .tokenType("Bearer ")
                        .build();
            }
        }
        return null;
    }
}
