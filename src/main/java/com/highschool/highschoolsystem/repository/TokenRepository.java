package com.highschool.highschoolsystem.repository;

import com.highschool.highschoolsystem.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, String> {
    @Query("SELECT t FROM TokenEntity t WHERE t.user.userId = ?1 AND (t.expired = false OR t.revoked = false)")
    List<TokenEntity> findAllValidTokenByUserId(String userId);

    Optional<TokenEntity> findByToken(String token);
    Optional<TokenEntity> findByUserId(String userId);
    List<TokenEntity> findAllByUserId(String userId);
    Optional<TokenEntity> deleteByUserId(String userId);
}
