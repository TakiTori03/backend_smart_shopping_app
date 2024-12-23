package com.hust.smart_Shopping.services.impl;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hust.smart_Shopping.constants.AppConstants;
import com.hust.smart_Shopping.constants.Enum.TaskStatus;
import com.hust.smart_Shopping.dtos.shopping_list.AddTasksRequest;
import com.hust.smart_Shopping.dtos.shopping_list.AddTasksRequest.AddTagRequest;
import com.hust.smart_Shopping.exceptions.payload.BusinessLogicException;
import com.hust.smart_Shopping.exceptions.payload.DataNotFoundException;
import com.hust.smart_Shopping.exceptions.payload.PermissionDenyException;
import com.hust.smart_Shopping.models.Food;
import com.hust.smart_Shopping.models.ShoppingList;
import com.hust.smart_Shopping.models.Task;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.models.UserFamily;
import com.hust.smart_Shopping.repositories.FoodRepository;
import com.hust.smart_Shopping.repositories.ShoppingListRepository;
import com.hust.smart_Shopping.repositories.TaskRepository;
import com.hust.smart_Shopping.repositories.UserFamilyRepository;
import com.hust.smart_Shopping.repositories.UserRepository;
import com.hust.smart_Shopping.services.FoodService;
import com.hust.smart_Shopping.services.ShoppingListService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingListServiceImpl implements ShoppingListService {

    private final FoodService foodService;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final UserFamilyRepository userFamilyRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final TaskRepository taskRepository;

    @Override
    public ShoppingList createShoppingListInFamily(String name, String userName, String note, Instant date, User user) {
        UserFamily userFamily = userFamilyRepository.findByUser(user).orElseThrow(() -> new BusinessLogicException(""));
        if (!userFamily.getRole().getName().equals(AppConstants.RoleType.LEADER))
            throw new BusinessLogicException("");
        User member = userRepository.findByNickname(userName).orElseThrow(() -> new DataNotFoundException(""));
        ShoppingList shoppingListForUser = new ShoppingList();
        shoppingListForUser.setUser(member);
        shoppingListForUser.setFamily(userFamily.getFamily());
        shoppingListForUser.setDate(date);
        shoppingListForUser.setNote(note);
        shoppingListForUser.setName(name);

        log.debug("create shopping list: {}", shoppingListForUser);
        return shoppingListRepository.save(shoppingListForUser);

    }

    @Override
    public ShoppingList updateShoppingList(Long listId, String newName, String newUserName, Instant newDate,
            String newNote, User user) {

        UserFamily userFamily = userFamilyRepository.findByUser(user).orElseThrow(() -> new BusinessLogicException(""));
        if (!userFamily.getRole().getName().equals(AppConstants.RoleType.LEADER))
            throw new BusinessLogicException("");

        ShoppingList updateShoppingList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new DataNotFoundException(""));

        if (updateShoppingList.getFamily() != userFamily.getFamily())
            throw new PermissionDenyException("");

        User newMember = userRepository.findByNickname(newUserName).orElseThrow(() -> new DataNotFoundException(""));
        log.debug("update shopping list: {}", updateShoppingList);

        updateShoppingList.setName(newName);
        updateShoppingList.setUser(newMember);
        updateShoppingList.setDate(newDate);
        updateShoppingList.setNote(newNote);

        return shoppingListRepository.save(updateShoppingList);
    }

    @Override
    public void deleteShoppingList(Long listId, User user) {
        UserFamily userFamily = userFamilyRepository.findByUser(user).orElseThrow(() -> new BusinessLogicException(""));
        if (!userFamily.getRole().getName().equals(AppConstants.RoleType.LEADER))
            throw new BusinessLogicException("");

        ShoppingList deleteShoppingList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new DataNotFoundException(""));

        if (deleteShoppingList.getFamily() != userFamily.getFamily())
            throw new PermissionDenyException("");

        shoppingListRepository.delete(deleteShoppingList);
        log.debug("delete shoppign list: {}", deleteShoppingList);
    }

    @Override
    public void createTasksForList(Long listId, List<AddTagRequest> tasks, User user) {
        ShoppingList shoppingList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new DataNotFoundException(""));
        if (shoppingList.getUser() != user)
            throw new BusinessLogicException("");

        List<Task> newTasks = tasks.stream().map(taskRequest -> {
            Food food = foodRepository.findByName(taskRequest.getFoodName())
                    .orElseThrow(() -> new DataNotFoundException(""));
            Task task = new Task();
            task.setFood(food);
            task.setQuantity(taskRequest.getQuantity());
            task.setStatus(TaskStatus.NOT_DONE_YET);
            task.setShoppingList(shoppingList);
            return task;
        }).collect(Collectors.toList());

        log.debug("create tasks: {}", newTasks);
        taskRepository.saveAll(newTasks);
    }

    @Override
    public List<ShoppingList> getListOfTaskInFamily(User user) {
        UserFamily userFamily = userFamilyRepository.findByUser(user).orElseThrow(() -> new BusinessLogicException(""));
        return userFamily.getFamily().getShoppingLists();
    }

    @Override
    public void deleteTaskInFamily(Long taskId, User user) {
        UserFamily userFamily = userFamilyRepository.findByUser(user).orElseThrow(() -> new BusinessLogicException(""));
        if (!userFamily.getRole().getName().equals(AppConstants.RoleType.LEADER))
            throw new BusinessLogicException("");

        Task deleteTask = taskRepository.findById(taskId).orElseThrow(() -> new DataNotFoundException(""));

        if (deleteTask.getShoppingList().getFamily() != userFamily.getFamily())
            throw new BusinessLogicException("");
        taskRepository.delete(deleteTask);
        log.debug("delete task: {}", deleteTask);
    }

    @Override
    public void updateTaskInFamily(Long taskId, String newFoodName, Integer quantity, User user) {
        Task updateTask = taskRepository.findById(taskId).orElseThrow(() -> new DataNotFoundException(""));
        if (updateTask.getShoppingList().getUser() == user || (updateTask.getShoppingList().getFamily() != null
                ? updateTask.getShoppingList().getFamily().getLeader() == user
                : false)) {
            Food newFood = foodRepository.findByName(newFoodName).orElseThrow(() -> new DataNotFoundException(""));
            log.debug("update task: {}", updateTask);
            updateTask.setFood(newFood);
            updateTask.setQuantity(quantity);
            taskRepository.save(updateTask);
        } else {
            throw new PermissionDenyException("");
        }
    }

    @Override
    public void markTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new DataNotFoundException(""));
        if (task.getShoppingList().getUser() != user)
            throw new PermissionDenyException("");
        task.setStatus(TaskStatus.DONE);
        log.debug("mark task done: {}", task);
        taskRepository.save(task);
    }

}
