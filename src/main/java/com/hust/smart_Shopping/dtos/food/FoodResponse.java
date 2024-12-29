package com.hust.smart_Shopping.dtos.food;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hust.smart_Shopping.models.Category;
import com.hust.smart_Shopping.models.Food;
import com.hust.smart_Shopping.models.Unit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodResponse {
    private long id;
    private String imageUrl;
    private String type;
    private String name;
    private Long unitOfMeasurementId;
    private Long foodCategoryId;

    private Long userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;
    private UnitOfMeasurement unitOfMeasurement;
    private FoodCategory foodCategory;

    @Data
    @Builder
    public static class UnitOfMeasurement {
        private String unitName;

        public static UnitOfMeasurement fromUnit(Unit unit) {
            return UnitOfMeasurement.builder().unitName(unit.getName()).build();
        }
    }

    @Data
    @Builder
    public static class FoodCategory {
        private String categoryName;

        public static FoodCategory fromCategory(Category category) {
            return FoodCategory.builder().categoryName(category.getName()).build();
        }
    }

    public static FoodResponse fromFood(Food food) {
        return FoodResponse.builder()
                .id(food.getId())
                .imageUrl(food.getImageUrl())
                .name(food.getName())
                .type(food.getType())
                .unitOfMeasurementId(food.getUnit().getId())
                .foodCategoryId(food.getCategory().getId())
                .userId(food.getUser().getId())
                .createdAt(food.getCreatedAt())
                .updatedAt(food.getUpdatedAt())
                .build();

    }

    public static FoodResponse allFromFood(Food food) {
        return FoodResponse.builder()
                .id(food.getId())
                .imageUrl(food.getImageUrl())
                .name(food.getName())
                .type(food.getType())
                .unitOfMeasurementId(food.getUnit().getId())
                .unitOfMeasurement(UnitOfMeasurement.fromUnit(food.getUnit()))
                .foodCategory(FoodCategory.fromCategory(food.getCategory()))
                .foodCategoryId(food.getCategory().getId())
                .userId(food.getUser().getId())
                .createdAt(food.getCreatedAt())
                .updatedAt(food.getUpdatedAt())
                .build();

    }
}
