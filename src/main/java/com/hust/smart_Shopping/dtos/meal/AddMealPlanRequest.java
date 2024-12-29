package com.hust.smart_Shopping.dtos.meal;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class AddMealPlanRequest {

    private String foodName;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate timestamp;

    private String name;
}
