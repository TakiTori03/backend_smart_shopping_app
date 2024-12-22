package com.hust.smart_Shopping.services;

import java.time.LocalDate;
import java.util.List;

import com.hust.smart_Shopping.models.MealPlan;
import com.hust.smart_Shopping.models.User;

public interface MealPlanService {
    MealPlan createMealPlan(String foodName, LocalDate timestamp, String name, User user);

    void deletePlan(Long id, User user);

    MealPlan updateMealPlan(Long id, String newFoodName, String newName, User user);

    List<MealPlan> getAllByDate(LocalDate date, User user);
}
