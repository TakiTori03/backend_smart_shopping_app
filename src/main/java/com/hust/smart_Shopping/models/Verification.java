package com.hust.smart_Shopping.models;

import java.time.Instant;

import com.hust.smart_Shopping.constants.Enum.VerificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "verifications")
public class Verification extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "token")
    private String token;

    @Column(name = "expired_at")
    private Instant expiredAt;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VerificationType type;
}
