package com.hust.smart_Shopping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.smart_Shopping.models.Food;
import com.hust.smart_Shopping.models.Recipe;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    boolean existsByName(String name);

    List<Recipe> findByFood(Food food);
}
