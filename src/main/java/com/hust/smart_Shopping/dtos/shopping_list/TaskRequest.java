package com.hust.smart_Shopping.dtos.shopping_list;

import lombok.Data;

@Data
public class TaskRequest {
    private Long taskId;
    private String newFoodName;
    private Integer quantity;
}
