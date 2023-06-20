package com.highschool.highschoolsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highschool.highschoolsystem.auth.AuthenticationRequest;
import com.highschool.highschoolsystem.auth.AuthenticationResponse;
import com.highschool.highschoolsystem.auth.RegistrationRequest;
import com.highschool.highschoolsystem.config.Role;
import com.highschool.highschoolsystem.config.TokenType;
import com.highschool.highschoolsystem.converter.UserConverter;
import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.entity.TeacherEntity;
import com.highschool.highschoolsystem.entity.TokenEntity;
import com.highschool.highschoolsystem.entity.UserEntity;
import com.highschool.highschoolsystem.exception.UserExistException;
import com.highschool.highschoolsystem.exception.UserNotFoundException;
import com.highschool.highschoolsystem.repository.StudentRepository;
import com.highschool.highschoolsystem.repository.TeacherRepository;
import com.highschool.highschoolsystem.repository.TokenRepository;
import com.highschool.highschoolsystem.repository.UserRepository;
import com.highschool.highschoolsystem.util.principal.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserPrincipal userPrincipal;
        switch (request.getRole()) {
            case "teacher": {
//               throw UserNotFoundException
                TeacherEntity teacher = teacherRepository.findByName(request.getUsername()).orElseThrow(
                        () -> new UserNotFoundException("User " + request.getUsername() + " not found")
                );
                userPrincipal = new UserPrincipal(teacher);
                String token = jwtService.generateToken(userPrincipal);
                String refreshToken = jwtService.generateRefreshToken(userPrincipal);

                UserEntity user = userRepository.findByUserId(teacher.getId()).orElseThrow();
                revokeAllToken(user);
                saveUserToken(user, token);

                return AuthenticationResponse.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .tokenType("Bearer ")
                        .build();
            }
            case "student": {
                StudentEntity student = studentRepository.findByName(request.getUsername()).orElseThrow(
                        () -> new UserNotFoundException("User " + request.getUsername() + " not found")
                );
                userPrincipal = new UserPrincipal(student);
                String token = jwtService.generateToken(userPrincipal);
                String refreshToken = jwtService.generateRefreshToken(userPrincipal);

                UserEntity user = userRepository.findByUserId(student.getId()).orElseThrow();
                revokeAllToken(user);
                saveUserToken(user, token);

                return AuthenticationResponse.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .tokenType("Bearer ")
                        .build();
            }
        }
        throw new UserNotFoundException("User " + request.getUsername() + " not found");
    }

    public AuthenticationResponse register(RegistrationRequest request, String role) throws UserExistException {
        UserPrincipal userPrincipal;
        switch (role) {
            case "teacher": {
                TeacherEntity teacherEntity = teacherRepository.findByName(request.getUsername()).orElse(null);
                if (teacherEntity != null) {
                    throw new UserExistException("User " + request.getUsername() + " already exists");
                }
                String password = passwordEncoder.encode(request.getPassword());
                TeacherEntity teacher = TeacherEntity.builder()
                        .name(request.getUsername())
                        .fullName(request.getFullName())
                        .password(password)
                        .role(Role.TEACHER)
                        .build();



                userPrincipal = new UserPrincipal(teacher);
                teacherRepository.save(teacher);
                UserEntity user = UserEntity.builder()
                        .username(request.getUsername())
                        .password(password)
                        .role(teacher.getRole())
                        .userId(teacher.getId())
                        .build();
                userRepository.save(user);
                String token = jwtService.generateToken(userPrincipal);
                String refreshToken = jwtService.generateRefreshToken(userPrincipal);
                saveUserToken(user, token);
                return AuthenticationResponse.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .tokenType(TokenType.BEARER.getTokenType())
                        .build();
            }
            case "student" : {
                StudentEntity studentEntity = studentRepository.findByName(request.getUsername()).orElse(null);
                if (studentEntity != null) {
                    throw new UserExistException("User " + request.getUsername() + " already exists");
                }
                String password = passwordEncoder.encode(request.getPassword());
                StudentEntity student = StudentEntity.builder()
                        .name(request.getUsername())
                        .fullName(request.getFullName())
                        .password(password)
                        .role(Role.STUDENT)
                        .build();

                userPrincipal = new UserPrincipal(student);
                studentRepository.save(student);
                UserEntity user = UserEntity.builder()
                        .username(request.getUsername())
                        .password(password)
                        .role(student.getRole())
                        .userId(student.getId())
                        .build();
                userRepository.save(user);
                String token = jwtService.generateToken(userPrincipal);
                String refreshToken = jwtService.generateRefreshToken(userPrincipal);
                saveUserToken(user, token);
                return AuthenticationResponse.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .tokenType(TokenType.BEARER.getTokenType())
                        .build();
            }
        }
        return null;
    }

    private void revokeAllToken(UserEntity user) {
        tokenRepository.findAllValidTokenByUserId(user.getUserId())
                .forEach(token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                });
        tokenRepository.saveAll(user.getTokens());
    }
    private void saveUserToken(UserEntity user, String jwtToken) {
        TokenEntity token = TokenEntity.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String authorizeHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;
        String username;

        if (authorizeHeader == null || !authorizeHeader.startsWith(TokenType.BEARER.getTokenType())) {
            return;
        }

        refreshToken = authorizeHeader.substring(TokenType.BEARER.getTokenType().length());

        username = jwtService.extractUsername(refreshToken);

        if (username == null) {
            return;
        }

        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        UserPrincipal userPrincipal = UserConverter.toPrincipal(user);

        if (!jwtService.isTokenValid(refreshToken, userPrincipal)) {
            return;
        }

        String token = jwtService.generateToken(userPrincipal);

        revokeAllToken(user);
        saveUserToken(user, token);

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER.getTokenType())
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
    }
}
