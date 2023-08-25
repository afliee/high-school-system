package com.highschool.highschoolsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highschool.highschoolsystem.auth.*;
import com.highschool.highschoolsystem.config.Role;
import com.highschool.highschoolsystem.config.TokenType;
import com.highschool.highschoolsystem.converter.UserConverter;
import com.highschool.highschoolsystem.entity.*;
import com.highschool.highschoolsystem.exception.UserExistException;
import com.highschool.highschoolsystem.exception.UserNotFoundException;
import com.highschool.highschoolsystem.repository.*;
import com.highschool.highschoolsystem.util.RandomStringUtils;
import com.highschool.highschoolsystem.util.mail.EmailDetails;
import com.highschool.highschoolsystem.util.principal.UserPrincipal;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationService {

    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


//    private final EmailService emailService;
    @Autowired
    private EmailService emailService;
    public AuthenticationResponse authenticate(
            AuthenticationRequest request,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
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

                Cookie cookie = new Cookie("refreshToken", refreshToken);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

                Cookie tokenCookie = new Cookie("token", token);
                tokenCookie.setHttpOnly(true);
                tokenCookie.setPath("/");
                tokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

                httpServletResponse.addCookie(tokenCookie);
                httpServletResponse.addCookie(cookie);

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

                Cookie cookie = new Cookie("refreshToken", refreshToken);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

                Cookie tokenCookie = new Cookie("token", token);
                tokenCookie.setHttpOnly(true);
                tokenCookie.setPath("/");
                tokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

                httpServletResponse.addCookie(tokenCookie);
                httpServletResponse.addCookie(cookie);

                return AuthenticationResponse.builder()
                        .token(token)
                        .refreshToken(refreshToken)
                        .tokenType("Bearer ")
                        .build();
            }
            case "administrator": {
                AdminEntity admin = adminRepository.findByUsername(request.getUsername()).orElseThrow(
                        () -> new UserNotFoundException("User " + request.getUsername() + " not found")
                );

                userPrincipal = UserConverter.toPrincipal(admin);
                String token = jwtService.generateToken(userPrincipal);
                String refreshToken = jwtService.generateRefreshToken(userPrincipal);

                UserEntity user = userRepository.findByUserId(admin.getId()).orElseThrow();
                revokeAllToken(user);
                saveUserToken(user, token);

                Cookie cookie = new Cookie("refreshToken", refreshToken);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days


                Cookie tokenCookie = new Cookie("token", token);
                tokenCookie.setHttpOnly(true);
                tokenCookie.setPath("/");
                tokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

                httpServletResponse.addCookie(cookie);
                httpServletResponse.addCookie(tokenCookie);

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
                        .email(request.getEmail())
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
                        .email(request.getEmail())
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
            case "administrator": {
                UserEntity userEntity = userRepository.findByUsername(request.getUsername()).orElse(null);
                if (userEntity != null) {
                    throw new UserExistException("User " + request.getUsername() + " already exists");
                }
                String password = passwordEncoder.encode(request.getPassword());
                AdminEntity admin = AdminEntity.builder()
                        .username(request.getUsername())
                        .password(password)
                        .fullName(request.getFullName())
                        .email(request.getEmail())
                        .role(Role.ADMIN)
                        .build();

                userPrincipal = UserConverter.toPrincipal(admin);
                adminRepository.save(admin);
                UserEntity user = UserEntity.builder()
                        .username(request.getUsername())
                        .password(password)
                        .role(admin.getRole())
                        .userId(admin.getId())
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
        throw new RuntimeException("Something went wrong with registration request");
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
            throw new RuntimeException("Refresh token is invalid or expired");
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

    public AuthenticationResponse resetPassword(HttpServletRequest request ,ResetPasswordRequest resetPasswordRequest) {
        var header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(TokenType.BEARER.getTokenType())) {
            throw new RuntimeException("Token is invalid or expired");
        }
        final String token = header.substring(TokenType.BEARER.getTokenType().length());
        var user = userRepository.findByUsername(resetPasswordRequest.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User " + resetPasswordRequest + " not found")
        );

        UserPrincipal userPrincipal = UserConverter.toPrincipal(user);

        if (!jwtService.isTokenValid(token, userPrincipal)) {
            throw new RuntimeException("Token is invalid or expired");
        }

        String password = passwordEncoder.encode(resetPasswordRequest.getNewPassword());

        final String role = user.getRole().toString();

        if (role.equalsIgnoreCase("TEACHER")) {
            var teacher = teacherRepository.findById(user.getUserId()).orElseThrow();

            teacher.setPassword(password);
            teacherRepository.save(teacher);
        } else if (role.equalsIgnoreCase("STUDENT")) {
            var student = studentRepository.findById(user.getUserId()).orElseThrow();

            student.setPassword(password);
            studentRepository.save(student);
        } else {
            throw new RuntimeException("Something went wrong");
        }

        user.setPassword(password);
        userRepository.save(user);
        var userTarget = UserConverter.toPrincipal(user);
        final String accessToken = jwtService.generateToken(userTarget);
        final String refreshToken = jwtService.generateRefreshToken(UserConverter.toPrincipal(user));
        revokeAllToken(user);
        saveUserToken(user, accessToken);
        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER.getTokenType())
                .build();
    }

    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User " + request.getUsername() + " not found")
        );

        final String role = user.getRole().toString();
        final String email;
        if (role.equalsIgnoreCase("TEACHER")) {
            var teacher = teacherRepository.findById(user.getUserId()).orElseThrow();

            email = teacher.getEmail();
        } else if (role.equalsIgnoreCase("STUDENT")) {
            var student = studentRepository.findById(user.getUserId()).orElseThrow();

            email = student.getEmail();
        } else if (role.equalsIgnoreCase("ADMIN")) {
            var admin = adminRepository.findById(user.getUserId()).orElseThrow();

            email = admin.getEmail();
        } else {
            throw new RuntimeException("Something went wrong");
        }

        if (email == null) {
            throw new RuntimeException("Email is not set");
        }

        String resetCode = generateResetCode();

        user.setResetCode(resetCode);
        userRepository.save(user);
        var emailDetails = EmailDetails.builder()
                .to(email)
                .subject("Reset password")
                .content(resetCode)
                .build();

        emailService.sendEmail(emailDetails);
        return ForgotPasswordResponse.builder()
                .message("Reset code has been sent to your email")
                .build();
    }

    public AuthenticationResponse forgotPasswordConfirm(ForgotConfirmRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User " + request.getUsername() + " not found")
        );

        final String resetCode = user.getResetCode();
        if (!resetCode.equals(request.getCode())) {
            throw new RuntimeException("Reset code is invalid");
        }

        final String password = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(password);
        userRepository.save(user);

        final String role = user.getRole().toString();
        UserDetails userPrincipal;
        if (role.equalsIgnoreCase("TEACHER")) {
            var teacher = teacherRepository.findById(user.getUserId()).orElseThrow();
            teacher.setPassword(password);
            userPrincipal = UserConverter.toPrincipal(teacher);
            teacherRepository.save(teacher);
        } else if (role.equalsIgnoreCase("STUDENT")) {
            var student = studentRepository.findById(user.getUserId()).orElseThrow();
            student.setPassword(password);
            userPrincipal = UserConverter.toPrincipal(student);
            studentRepository.save(student);
        } else if (role.equalsIgnoreCase("ADMIN")) {
            var admin = adminRepository.findById(user.getUserId()).orElseThrow();
            admin.setPassword(password);
            userPrincipal = UserConverter.toPrincipal(admin);
            adminRepository.save(admin);
        } else {
            throw new RuntimeException("Something went wrong");
        }

        final String token = jwtService.generateToken(userPrincipal);
        final String refreshToken = jwtService.generateRefreshToken(userPrincipal);

        revokeAllToken(user);
        saveUserToken(user, token);
        user.setResetCode(null);
        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .tokenType(TokenType.BEARER.getTokenType())
                .build();
    }


    private String generateResetCode() {
//        random number include 6 number
        return RandomStringUtils.randomAlphanumeric(6);
    }
}
