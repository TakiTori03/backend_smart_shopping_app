package com.hust.smart_Shopping.dtos.shopping_list;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ShoppingListRequest {
    private Long listId;
    private String newName;
    private String newAssignToUsername;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate newDate;
    private String newNote;
}
