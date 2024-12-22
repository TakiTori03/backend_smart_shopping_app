package com.hust.smart_Shopping.dtos.fridge;

import lombok.Data;

@Data
public class FridgeRequest {
    private Long itemId;
    private String newNote;
    private Integer newQuantity;
    private Long newUseWithin;
    private String newFoodName;
}
