package com.hust.smart_Shopping.dtos.fridge;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hust.smart_Shopping.dtos.food.FoodResponse;
import com.hust.smart_Shopping.models.FridgeItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FridgeItemResponse {
    private Long id;
    private Integer quantity;
    private String note;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant expiredDate;
    private Long foodId;
    private Long userId;
    private FoodResponse food;

    public static FridgeItemResponse fromFridgeItem(FridgeItem item) {
        return FridgeItemResponse.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .note(item.getNote())
                .startDate(item.getStartDate())
                .expiredDate(item.getExpiredDate())
                .foodId(item.getFood().getId())
                .userId(item.getUser().getId())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    public static FridgeItemResponse allFromFridgeItem(FridgeItem item) {
        return FridgeItemResponse.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .note(item.getNote())
                .startDate(item.getStartDate())
                .expiredDate(item.getExpiredDate())
                .foodId(item.getFood().getId())
                .userId(item.getUser().getId())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .food(FoodResponse.fromFood(item.getFood()))
                .build();
    }

    public static FridgeItemResponse specificFromFridgeItem(FridgeItem item) {
        return FridgeItemResponse.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .note(item.getNote())
                .startDate(item.getStartDate())
                .expiredDate(item.getExpiredDate())
                .foodId(item.getFood().getId())
                .userId(item.getUser().getId())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .food(FoodResponse.allFromFood(item.getFood()))
                .build();
    }
}
