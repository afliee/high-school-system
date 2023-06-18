package com.highschool.highschoolsystem.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<User> {
    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return Optional.of((User) authentication.getPrincipal());
        }
        return Optional.empty();
    }
}
