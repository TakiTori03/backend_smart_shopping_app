package com.hust.smart_Shopping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.models.Verification;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

    Optional<Verification> findByCode(String code);

    Optional<Verification> findByUser(User user);
}
