package com.hust.smart_Shopping.dtos.meal;

import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hust.smart_Shopping.dtos.food.FoodResponse;
import com.hust.smart_Shopping.models.MealPlan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MealPlanResponse {
    private Long id;
    private String name;
    private String status;

    @JsonFormat(pattern = "M/d/yyyy")
    private LocalDate timestamp;
    private Long foodId;
    private Long userId;
    private Instant createdAt;
    private Instant updatedAt;
    private FoodResponse food;

    public static MealPlanResponse fromMealPlan(MealPlan mealPlan) {
        return MealPlanResponse.builder()
                .id(mealPlan.getId())
                .name(mealPlan.getName() != null ? mealPlan.getName().toString() : null)
                .status(mealPlan.getStatus() != null ? mealPlan.getStatus().toString() : null)
                .timestamp(mealPlan.getTimestamp())
                .foodId(mealPlan.getFood().getId())
                .userId(mealPlan.getUser().getId())
                .createdAt(mealPlan.getCreatedAt())
                .updatedAt(mealPlan.getUpdatedAt())
                .build();
    }

    public static MealPlanResponse allFromMealPlan(MealPlan mealPlan) {
        return MealPlanResponse.builder()
                .id(mealPlan.getId())
                .name(mealPlan.getName() != null ? mealPlan.getName().toString() : null)
                .status(mealPlan.getStatus() != null ? mealPlan.getStatus().toString() : null)
                .timestamp(mealPlan.getTimestamp())
                .foodId(mealPlan.getFood().getId())
                .userId(mealPlan.getUser().getId())
                .createdAt(mealPlan.getCreatedAt())
                .updatedAt(mealPlan.getUpdatedAt())
                .food(FoodResponse.allFromFood(mealPlan.getFood()))
                .build();
    }
}
