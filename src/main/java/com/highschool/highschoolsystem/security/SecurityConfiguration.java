package com.highschool.highschoolsystem.security;

import com.highschool.highschoolsystem.config.JwtAuthenticationFilter;
import com.highschool.highschoolsystem.config.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final String[] publicRoutes = new String[] {
            "/",
            "/app/**",
            "/topic/**",
            "/ws/**",
            "/login",
            "/register",
            "/teacher/**",
            "/download/**",
            "/student/**",
            "/attendance/**",
            "/forgot-password",
            "/auth/admin/**",
            "/navigator/**",
            "/topic/**",
            "/error/**",
            "/webjars/**",
            "/vendors/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/uploads/**",
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                            authorize
                                    .requestMatchers(publicRoutes)
                                    .permitAll()
                                    .requestMatchers(new String[]{
                                            "/api/v1/admin/**"
                                    }).hasRole(Role.ADMIN.name())
                                    .requestMatchers(new String[]{
                                            "/api/v1/teacher/**"
                                    }).hasAnyRole(
                                            Role.ADMIN.name(),
                                            Role.TEACHER.name()
                                    )
                                    .anyRequest().authenticated();
                        }
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/403")
                )
                .authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(
                                (request, response, authentication) -> SecurityContextHolder.clearContext()
                        )
                );
        return http.build();
    }
}