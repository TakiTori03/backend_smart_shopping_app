package com.hust.smart_Shopping.dtos.recipe;

import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;
import com.hust.smart_Shopping.dtos.food.FoodResponse;
import com.hust.smart_Shopping.models.Recipe;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeResponse {
    private Long id;
    private String name;
    private String description;
    private JsonNode htmlContent;
    private Long foodId;
    private Long userId;
    private Instant createdAt;
    private Instant updatedAt;
    private FoodResponse food;

    public static RecipeResponse fromRecipe(Recipe recipe) {
        return RecipeResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .htmlContent(recipe.getHtmlContent())
                .foodId(recipe.getFood().getId())
                .userId(recipe.getUser().getId())
                .food(FoodResponse.fromFood(recipe.getFood()))
                .build();
    }
}
