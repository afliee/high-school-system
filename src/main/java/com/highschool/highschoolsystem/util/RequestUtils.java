package com.highschool.highschoolsystem.util;

import com.highschool.highschoolsystem.config.TokenType;
import com.highschool.highschoolsystem.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils {
    private static JwtService jwtService;
    public static String requireHeader(String headerName, HttpServletRequest request) {
        var header = request.getHeader(headerName);
        return header;
    }

    public static String requireLogin(HttpServletRequest request) {
        return requireHeader("Authorization", request);
    }
}
