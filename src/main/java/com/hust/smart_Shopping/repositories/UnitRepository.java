package com.hust.smart_Shopping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.smart_Shopping.models.Unit;
import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    boolean existsByName(String name);

    Optional<Unit> findByName(String name);
}
