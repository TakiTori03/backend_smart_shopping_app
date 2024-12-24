package com.hust.smart_Shopping.services.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.smart_Shopping.constants.Enum.MealStatus;
import com.hust.smart_Shopping.constants.Enum.MealType;
import com.hust.smart_Shopping.exceptions.payload.BusinessLogicException;
import com.hust.smart_Shopping.exceptions.payload.DataNotFoundException;
import com.hust.smart_Shopping.models.Food;
import com.hust.smart_Shopping.models.MealPlan;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.repositories.FoodRepository;
import com.hust.smart_Shopping.repositories.MealPlanRepository;
import com.hust.smart_Shopping.services.MealPlanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MealPlanServiceImpl implements MealPlanService {

    private final MealPlanRepository mealPlanRepository;
    private final FoodRepository foodRepository;

    @Override
    public MealPlan createMealPlan(String foodName, LocalDate timestamp, String name, User user) {
        Food food = foodRepository.findByName(foodName).orElseThrow(() -> new DataNotFoundException(""));

        MealType mealName = MealType.valueOf(name.toUpperCase());

        MealPlan newMealPlan = new MealPlan();
        newMealPlan.setFood(food);
        newMealPlan.setName(mealName);
        newMealPlan.setStatus(MealStatus.NOT_PASS_YET);
        newMealPlan.setUser(user);
        newMealPlan.setTimestamp(timestamp);

        log.debug("create new meal plan: {}", newMealPlan);

        return mealPlanRepository.save(newMealPlan);
    }

    @Override
    public void deletePlan(Long id, User user) {
        MealPlan deleteMealPlan = mealPlanRepository.findById(id).orElseThrow(() -> new DataNotFoundException(""));
        if (deleteMealPlan.getUser() != user)
            throw new BusinessLogicException("");
        mealPlanRepository.delete(deleteMealPlan);
        log.debug("delete mealplan : {}", deleteMealPlan);
    }

    @Override
    public MealPlan updateMealPlan(Long id, String newFoodName, String newName, User user) {
        MealPlan updateMealPlan = mealPlanRepository.findById(id).orElseThrow(() -> new DataNotFoundException(""));
        if (updateMealPlan.getUser() != user)
            throw new BusinessLogicException("");
        log.debug("update mealplan: {}", updateMealPlan);
        MealType newMealName = MealType.valueOf(newName.toUpperCase());

        Food food = foodRepository.findByName(newFoodName).orElseThrow(() -> new DataNotFoundException(""));
        updateMealPlan.setFood(food);
        updateMealPlan.setName(newMealName);

        return mealPlanRepository.save(updateMealPlan);

    }

    @Override
    public List<MealPlan> getAllByDate(LocalDate date, User user) {
        return mealPlanRepository.findByUserAndTimestamp(user, date);
    }

}
