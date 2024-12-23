package com.hust.smart_Shopping.dtos.shopping_list;

import java.util.List;

import lombok.Data;

@Data
public class AddTasksRequest {
    private Long listId;
    private List<AddTagRequest> tasks;

    @Data
    public static class AddTagRequest {
        private String foodName;
        private Integer quantity;

    }

}
