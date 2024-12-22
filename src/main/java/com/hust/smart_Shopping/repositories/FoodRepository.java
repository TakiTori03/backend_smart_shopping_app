package com.hust.smart_Shopping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.smart_Shopping.models.Food;
import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {
    boolean existsByName(String name);

    Optional<Food> findByName(String name);
}
