package com.hust.smart_Shopping.dtos.food;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FoodRequest {
    private String name;
    private String foodCategoryName;
    private String unitName;
    private MultipartFile image;
}
