package com.hust.smart_Shopping.services;

import java.util.List;

import com.hust.smart_Shopping.models.FridgeItem;
import com.hust.smart_Shopping.models.User;

public interface FridgeService {
    FridgeItem createFridgeItem(String name, Long time, Integer quantity, User user);

    FridgeItem updateFridgeItem(Long id, String newNote, Integer newQuantity, Long newTime, String newName);

    void deleteFridgeItem(String foodName, User user);

    List<FridgeItem> getAllFridgeItemsWithUser(User user);

    List<FridgeItem> getSpecificItem(String foodName);
}
