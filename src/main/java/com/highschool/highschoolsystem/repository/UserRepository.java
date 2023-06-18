package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
}
