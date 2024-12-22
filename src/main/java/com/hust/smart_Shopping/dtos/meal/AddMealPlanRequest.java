package com.hust.smart_Shopping.dtos.meal;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AddMealPlanRequest {

    private String foodName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private LocalDate timestamp;

    private String name;
}
