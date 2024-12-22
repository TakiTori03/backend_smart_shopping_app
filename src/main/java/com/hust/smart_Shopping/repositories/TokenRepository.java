package com.hust.smart_Shopping.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.smart_Shopping.models.Token;
import com.hust.smart_Shopping.models.User;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    Optional<Token> findByUserId(Long userId);

    List<Token> findByUser(User user);

    Optional<Token> findByRefreshToken(String refreshToken);
}
