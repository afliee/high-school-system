package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminEntity, String> {
    Optional<AdminEntity> findByUsername(String username);
}
