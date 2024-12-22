package com.hust.smart_Shopping.dtos.recipe;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class AddRecipeRequest {
    private String foodName;
    private String name;
    private String htmlContent;
    private String description;
}
