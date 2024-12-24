package com.hust.smart_Shopping.dtos.shopping_list;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class AddShoppingListRequest {
    private String name;
    private String assignToUsername;
    private String note;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate date;
}
