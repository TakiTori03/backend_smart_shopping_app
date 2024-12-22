package com.hust.smart_Shopping.dtos.meal;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AddMealPlanRequest {

    private String foodName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate timestamp;

    private String name;
}
