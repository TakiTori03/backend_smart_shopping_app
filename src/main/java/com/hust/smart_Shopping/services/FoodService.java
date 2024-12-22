package com.hust.smart_Shopping.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hust.smart_Shopping.models.Food;
import com.hust.smart_Shopping.models.User;

public interface FoodService {
    Food createFood(String name, String foodCategoryName, String unitName, MultipartFile image, User user);

    Food updateFood(String name, String foodCategoryName, String unitName, MultipartFile image, User user);

    void deleteFood(String name);

    List<Food> getAllFoods();
}
