package com.hust.smart_Shopping.models;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class Token extends BaseEntity {

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "token_type", nullable = false, length = 50)
    private String tokenType;

    @Column(name = "expiration_time", nullable = false)
    private Instant expirationTime; // thời gian sống của token

    @Column(name = "refresh_expiration_time", nullable = false)
    private Instant refreshExpirationTime; // thời gian sống của refresh token

    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    @Column(name = "expired", nullable = false)
    private boolean expired;

    @Column(name = "is_mobile")
    private boolean isMobile;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
