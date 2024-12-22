package com.hust.smart_Shopping.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.smart_Shopping.models.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String name);
}
