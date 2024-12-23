package com.hust.smart_Shopping.dtos.shopping_list;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AddShoppingListRequest {
    private String name;
    private String assignToUsername;
    private String note;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private Instant date;
}
