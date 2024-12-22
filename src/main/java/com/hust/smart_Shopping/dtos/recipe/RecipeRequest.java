package com.hust.smart_Shopping.dtos.recipe;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class RecipeRequest {
    private Long recipeId;
    private String newHtmlContent;
    private String newDescription;
    private String newFoodName;
    private String newName;

}
