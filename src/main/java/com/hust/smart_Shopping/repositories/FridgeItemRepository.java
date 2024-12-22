package com.hust.smart_Shopping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.smart_Shopping.models.Food;
import com.hust.smart_Shopping.models.FridgeItem;
import java.util.List;
import java.util.Optional;

import com.hust.smart_Shopping.models.User;

public interface FridgeItemRepository extends JpaRepository<FridgeItem, Long> {
    boolean existsByFood(Food food);

    List<FridgeItem> findByFoodAndUser(Food food, User user);

    List<FridgeItem> findByUser(User user);

    List<FridgeItem> findByFood(Food food);

}
