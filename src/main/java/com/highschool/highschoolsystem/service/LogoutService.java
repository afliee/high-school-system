package com.highschool.highschoolsystem.service;

import com.highschool.highschoolsystem.config.TokenType;
import com.highschool.highschoolsystem.entity.TokenEntity;
import com.highschool.highschoolsystem.exception.MissingParametersException;
import com.highschool.highschoolsystem.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.util.MissingResourceException;
import java.util.logging.Logger;

@Service
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class LogoutService implements LogoutHandler {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws MissingParametersException {
        String authorizationHeader = request.getHeader("Authorization");
        String token;

        if (authorizationHeader == null || !authorizationHeader.startsWith(TokenType.BEARER.getTokenType())) {
//            log warning
            System.out.println("Authorization header is null or does not start with Bearer");
            throw  new MissingParametersException("Authorization header is null or does not start with Bearer");
        }

        token = authorizationHeader.substring(TokenType.BEARER.getTokenType().length());

        TokenEntity storedToken = tokenRepository.findByToken(token).orElse(null);

        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }

        var tokenCookie = WebUtils.getCookie(request, "token");
        if (tokenCookie != null && !tokenCookie.getValue().isEmpty()) {
            tokenCookie.setValue(null);
            tokenCookie.setMaxAge(0);
            tokenCookie.setPath("/");
            tokenCookie.setHttpOnly(true);
            response.addCookie(tokenCookie);
        }
    }
}
