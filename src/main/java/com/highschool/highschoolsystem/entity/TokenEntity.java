package com.highschool.highschoolsystem.entity;

import com.highschool.highschoolsystem.config.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "token")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity extends BaseEntity<String> {
    private String token;
    private boolean revoked;
    private boolean expired;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
