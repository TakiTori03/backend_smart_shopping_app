package com.hust.smart_Shopping.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.smart_Shopping.exceptions.payload.BusinessLogicException;
import com.hust.smart_Shopping.exceptions.payload.DataNotFoundException;
import com.hust.smart_Shopping.models.Food;
import com.hust.smart_Shopping.models.Recipe;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.repositories.FoodRepository;
import com.hust.smart_Shopping.repositories.RecipeRepository;
import com.hust.smart_Shopping.services.RecipeService;
import com.hust.smart_Shopping.utils.MessageKeys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final FoodRepository foodRepository;

    @Override
    public Recipe createRecipe(String foodName, String name, String htmlContent, String description, User user) {
        if (recipeRepository.existsByName(name))
            throw new BusinessLogicException(MessageKeys.RECIPE_EXIST);
        Food food = foodRepository.findByName(foodName)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
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
    public Recipe updateRecipe(Long recipeID, String newHtmlContent, String newDescription, String newFoodName,
            String newName, User user) {
        Recipe updateRecipe = recipeRepository.findById(recipeID)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
        if (updateRecipe.getUser() != user)
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);
        if (recipeRepository.existsByName(newName))
            throw new BusinessLogicException(MessageKeys.RECIPE_EXIST);

        Food newFood = foodRepository.findByName(newFoodName)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
        log.debug("update recipe: {}", updateRecipe);
        updateRecipe.setHtmlContent(newHtmlContent);
        updateRecipe.setDescription(newDescription);
        updateRecipe.setFood(newFood);
        updateRecipe.setName(newName);

        return recipeRepository.save(updateRecipe);

    }

    @Override
    public void deleteRecipe(Long recipeId, User user) {
        Recipe deleteRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
        if (deleteRecipe.getUser() != user)
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);

        recipeRepository.delete(deleteRecipe);
        log.debug("delete recipe: {}", deleteRecipe);
    }

    @Override
    public List<Recipe> getAllByFoodId(Long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
        return recipeRepository.findByFood(food);
    }

}
