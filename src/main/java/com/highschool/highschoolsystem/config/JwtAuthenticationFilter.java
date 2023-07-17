package com.highschool.highschoolsystem.config;

import com.highschool.highschoolsystem.entity.TokenEntity;
import com.highschool.highschoolsystem.repository.TokenRepository;
import com.highschool.highschoolsystem.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authorizationHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            Boolean isValidToken = tokenRepository.findByToken(jwt)
                    .map(tokenEntity -> !tokenEntity.isExpired() && !tokenEntity.isRevoked())
                    .orElse(false);

            if (jwtService.isTokenValid(jwt, userDetails) && isValidToken) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

//                update security context holder
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                filterChain.doFilter(request, response);

            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            }
        }
    }
}
