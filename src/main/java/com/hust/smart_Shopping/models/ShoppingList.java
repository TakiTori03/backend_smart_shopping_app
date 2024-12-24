package com.hust.smart_Shopping.models;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shopping_lists")
public class ShoppingList extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "note")
    private String note;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shoppingList", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Task> tasks = new ArrayList<>();
}
