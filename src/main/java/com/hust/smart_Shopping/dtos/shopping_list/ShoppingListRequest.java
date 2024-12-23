package com.hust.smart_Shopping.dtos.shopping_list;

import java.time.Instant;

import lombok.Data;

@Data
public class ShoppingListRequest {
    private Long listId;
    private String newName;
    private String newAssignToUsername;
    private Instant newDate;
    private String newNote;
}
