package com.hust.smart_Shopping.models;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fridge_items")
public class FridgeItem extends BaseEntity {

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "expired_date")
    private Instant expiredDate;

    @Column(name = "note")
    private String note;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

}
