package com.hust.smart_Shopping.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "families")
public class Family extends BaseEntity {

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserFamily> userFamilies = new ArrayList<>();

    @OneToOne()
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "family", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ShoppingList> shoppingLists = new ArrayList<>();

}
