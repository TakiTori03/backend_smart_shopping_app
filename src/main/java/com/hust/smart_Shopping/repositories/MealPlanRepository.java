package com.hust.smart_Shopping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.smart_Shopping.models.MealPlan;
import java.util.List;
import java.time.LocalDate;
import com.hust.smart_Shopping.models.User;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
    List<MealPlan> findByUserAndTimestamp(User user, LocalDate timestamp);
}
