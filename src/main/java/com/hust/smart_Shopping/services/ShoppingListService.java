package com.hust.smart_Shopping.services;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import com.hust.smart_Shopping.dtos.shopping_list.AddTasksRequest.AddTagRequest;
import com.hust.smart_Shopping.models.ShoppingList;
import com.hust.smart_Shopping.models.User;

public interface ShoppingListService {
    ShoppingList createShoppingListInFamily(String name, String userName, String note, LocalDate date, User user);

    ShoppingList updateShoppingList(Long listId, String newName, String newUserName, LocalDate newDate, String newNote,
            User user);

    void deleteShoppingList(Long listId, User user);

    void createTasksForList(Long listId, List<AddTagRequest> tasks, User user);

    List<ShoppingList> getListOfTaskInFamily(User user);

    void deleteTaskInFamily(Long taskId, User user);

    void updateTaskInFamily(Long taskId, String newFoodName, Integer quantity, User user);

    void markTask(Long taskId, User user);
}
