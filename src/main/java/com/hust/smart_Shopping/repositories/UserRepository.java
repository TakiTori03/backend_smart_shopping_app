package com.hust.smart_Shopping.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.smart_Shopping.models.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByNickname(String nickname);

}
