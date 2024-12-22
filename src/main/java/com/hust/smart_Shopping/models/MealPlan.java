package com.hust.smart_Shopping.models;

import java.time.Instant;
import java.time.LocalDate;

import com.hust.smart_Shopping.constants.Enum.MealStatus;
import com.hust.smart_Shopping.constants.Enum.MealType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "meal_plans")
public class MealPlan extends BaseEntity {

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private MealType name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MealStatus status;

    @Column(name = "timestamp")
    private LocalDate timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;
}
