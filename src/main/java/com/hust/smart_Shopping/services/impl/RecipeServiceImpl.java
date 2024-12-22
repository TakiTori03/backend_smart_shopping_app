package com.hust.smart_Shopping.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hust.smart_Shopping.exceptions.payload.BusinessLogicException;
import com.hust.smart_Shopping.exceptions.payload.DataNotFoundException;
import com.hust.smart_Shopping.models.Food;
import com.hust.smart_Shopping.models.Recipe;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.repositories.FoodRepository;
import com.hust.smart_Shopping.repositories.RecipeRepository;
import com.hust.smart_Shopping.services.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final FoodRepository foodRepository;

    @Override
    public Recipe createRecipe(String foodName, String name, JsonNode htmlContent, String description, User user) {
        if (recipeRepository.existsByName(name))
            throw new BusinessLogicException("");
        Food food = foodRepository.findByName(foodName).orElseThrow(() -> new DataNotFoundException(""));
        Recipe newRecipe = new Recipe();
        newRecipe.setFood(food);
        newRecipe.setName(name);
        newRecipe.setHtmlContent(htmlContent);
        newRecipe.setDescription(description);
        newRecipe.setUser(user);

        log.debug("create new recipe: {}", newRecipe);
        return recipeRepository.save(newRecipe);
    }

    @Override
    public Recipe updateRecipe(Long recipeID, JsonNode newHtmlContent, String newDescription, String newFoodName,
            String newName, User user) {
        Recipe updateRecipe = recipeRepository.findById(recipeID).orElseThrow(() -> new DataNotFoundException(""));
        if (updateRecipe.getUser() != user)
            throw new BusinessLogicException("");
        if (recipeRepository.existsByName(newName))
            throw new BusinessLogicException("");

        Food newFood = foodRepository.findByName(newFoodName).orElseThrow(() -> new DataNotFoundException(""));
        log.debug("update recipe: {}", updateRecipe);
        updateRecipe.setHtmlContent(newHtmlContent);
        updateRecipe.setDescription(newDescription);
        updateRecipe.setFood(newFood);
        updateRecipe.setName(newName);

        return recipeRepository.save(updateRecipe);

    }

    @Override
    public void deleteRecipe(Long recipeId, User user) {
        Recipe deleteRecipe = recipeRepository.findById(recipeId).orElseThrow(() -> new DataNotFoundException(""));
        if (deleteRecipe.getUser() != user)
            throw new BusinessLogicException("");

        recipeRepository.delete(deleteRecipe);
        log.debug("delete recipe: {}", deleteRecipe);
    }

    @Override
    public List<Recipe> getAllByFoodId(Long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new DataNotFoundException(""));
        return recipeRepository.findByFood(food);
    }

}
