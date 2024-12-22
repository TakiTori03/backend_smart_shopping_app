package com.hust.smart_Shopping.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "categories")
public class Category extends BaseEntity {
    @Column(name = "name", unique = true, nullable = false)
    private String name;
}
