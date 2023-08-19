package com.highschool.highschoolsystem.exception;

import com.highschool.highschoolsystem.repository.TokenRepository;
import com.highschool.highschoolsystem.repository.UserRepository;
import com.highschool.highschoolsystem.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandle {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @ExceptionHandler(ExpiredJwtException.class)
    public String handleExpiredJwtException(ExpiredJwtException e) {
//        get the token from the exception
        String username = e.getClaims().getSubject();

//    token = username
        var user = userRepository.findByUsername(username).orElseThrow();

        switch (user.getRole()) {
            case STUDENT -> {
                return "redirect:/login?with=student";
            }
            case ADMIN -> {
                return "redirect:/auth/admin/login";
            }
            case TEACHER -> {
                return "redirect:/login?with=teacher";
            }
        }

        return "redirect:/?component=chooseLogin";
//        return "redirect:/auth/admin/login";
    }
}
