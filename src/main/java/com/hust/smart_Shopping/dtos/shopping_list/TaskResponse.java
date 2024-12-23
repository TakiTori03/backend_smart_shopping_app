package com.hust.smart_Shopping.dtos.shopping_list;

import com.hust.smart_Shopping.models.Task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    private String foodName;
    private Integer quantity;
    private String status;

    public static TaskResponse fromTask(Task task) {
        return TaskResponse.builder()
                .foodName(task.getFood().getName())
                .quantity(task.getQuantity())
                .status(task.getStatus().toString())
                .build();
    }
}
