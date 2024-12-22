package com.hust.smart_Shopping.dtos.meal;

import lombok.Data;

@Data
public class MealPlanRequest {
    private Long planId;
    private String newFoodName;
    private String newName;

}
