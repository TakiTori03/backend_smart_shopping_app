package com.hust.smart_Shopping.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "units")
public class Unit extends BaseEntity {
    @Column(name = "name", unique = true, nullable = false)
    private String name;
}
