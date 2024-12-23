package com.hust.smart_Shopping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.smart_Shopping.models.ShoppingList;
import com.hust.smart_Shopping.models.Token;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

}
