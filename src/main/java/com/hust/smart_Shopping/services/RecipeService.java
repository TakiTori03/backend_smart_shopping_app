package com.hust.smart_Shopping.services;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.hust.smart_Shopping.models.Recipe;
import com.hust.smart_Shopping.models.User;

public interface RecipeService {
    Recipe createRecipe(String foodName, String name, String htmlContent, String description, User user);

    Recipe updateRecipe(Long recipeID, String newHtmlContent, String newDescription, String newFoodName,
            String newName, User user);

    void deleteRecipe(Long recipeId, User user);

    List<Recipe> getAllByFoodId(Long foodId);
}
