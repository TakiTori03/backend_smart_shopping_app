package com.hust.smart_Shopping.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hust.smart_Shopping.exceptions.payload.BusinessLogicException;
import com.hust.smart_Shopping.exceptions.payload.DataNotFoundException;
import com.hust.smart_Shopping.models.Category;
import com.hust.smart_Shopping.models.Food;
import com.hust.smart_Shopping.models.Unit;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.repositories.CategoryRepository;
import com.hust.smart_Shopping.repositories.FoodRepository;
import com.hust.smart_Shopping.repositories.UnitRepository;
import com.hust.smart_Shopping.services.FoodService;
import com.hust.smart_Shopping.services.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodServiceImpl implements FoodService {

    private final ImageService imageService;
    private final FoodRepository foodRepository;
    private final UnitRepository unitRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Food createFood(String name, String foodCategoryName, String unitName, MultipartFile image, User user) {
        if (foodRepository.existsByName(name))
            throw new BusinessLogicException("");
        Category category = categoryRepository.findByName(foodCategoryName)
                .orElseThrow(() -> new BusinessLogicException(""));
        Unit unit = unitRepository.findByName(unitName).orElseThrow(() -> new BusinessLogicException(""));

        String imageUrl = imageService.uploadImage(image, "foods");

        Food newFood = new Food();
        newFood.setName(name);
        newFood.setImageUrl(imageUrl);
        newFood.setCategory(category);
        newFood.setUnit(unit);
        newFood.setUser(user);

        log.debug("create new food : {}", newFood);
        return foodRepository.save(newFood);

    }

    @Override
    public Food updateFood(String name, String foodCategoryName, String unitName, MultipartFile image, User user) {
        Food updateFood = foodRepository.findByName(name).orElseThrow(() -> new DataNotFoundException(""));

        Category category = categoryRepository.findByName(foodCategoryName)
                .orElseThrow(() -> new BusinessLogicException(""));
        Unit unit = unitRepository.findByName(unitName).orElseThrow(() -> new BusinessLogicException(""));

        String imageUrl = imageService.uploadImage(image, "foods");

        updateFood.setName(name);
        updateFood.setImageUrl(imageUrl);
        updateFood.setCategory(category);
        updateFood.setUnit(unit);
        updateFood.setUser(user);

        log.debug("update food : {}", updateFood);
        return foodRepository.save(updateFood);

    }

    @Override
    public void deleteFood(String name) {
        Food deleteFood = foodRepository.findByName(name).orElseThrow(() -> new DataNotFoundException(""));
        foodRepository.delete(deleteFood);
        log.debug("delete food: {}", deleteFood);
    }

    @Override
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

}
