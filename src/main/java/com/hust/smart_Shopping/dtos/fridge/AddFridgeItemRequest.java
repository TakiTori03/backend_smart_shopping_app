package com.hust.smart_Shopping.dtos.fridge;

import lombok.Data;

@Data
public class AddFridgeItemRequest {
    private String foodName;
    private Long useWithin; // tinh theo phut
    private Integer quantity;
}
