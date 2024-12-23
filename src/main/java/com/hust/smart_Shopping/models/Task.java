package com.hust.smart_Shopping.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hust.smart_Shopping.constants.Enum.TaskStatus;

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
@Table(name = "tasks")
public class Task extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "shopping_list_id")
    @JsonBackReference
    private ShoppingList shoppingList;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
}
