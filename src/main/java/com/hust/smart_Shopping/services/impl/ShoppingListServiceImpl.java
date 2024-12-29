package com.hust.smart_Shopping.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.smart_Shopping.constants.AppConstants;
import com.hust.smart_Shopping.constants.Enum.TaskStatus;

import com.hust.smart_Shopping.dtos.shopping_list.AddTasksRequest.AddTagRequest;
import com.hust.smart_Shopping.exceptions.payload.BusinessLogicException;
import com.hust.smart_Shopping.exceptions.payload.DataNotFoundException;
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
import com.hust.smart_Shopping.utils.MessageKeys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ShoppingListServiceImpl implements ShoppingListService {

    private final FoodService foodService;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final UserFamilyRepository userFamilyRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final TaskRepository taskRepository;

    @Override
    public ShoppingList createShoppingListInFamily(String name, String userName, String note, LocalDate date,
            User user) {
        UserFamily userFamily = userFamilyRepository.findByUser(user)
                .orElseThrow(() -> new BusinessLogicException(MessageKeys.YOU_NOT_CREATE_FAMILY));
        if (!userFamily.getRole().getName().equals(AppConstants.RoleType.LEADER))
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);
        User member = userRepository.findByNickname(userName)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));

        if (member.getUserFamily() == null)
            throw new BusinessLogicException(MessageKeys.USER_NOT_HAVED_FAMILY);

        if (member.getUserFamily().getFamily() != userFamily.getFamily()) {
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);
        }
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
    public ShoppingList updateShoppingList(Long listId, String newName, String newUserName, LocalDate newDate,
            String newNote, User user) {

        UserFamily userFamily = userFamilyRepository.findByUser(user)
                .orElseThrow(() -> new BusinessLogicException(MessageKeys.YOU_NOT_CREATE_FAMILY));
        if (!userFamily.getRole().getName().equals(AppConstants.RoleType.LEADER))
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);

        ShoppingList updateShoppingList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));

        if (updateShoppingList.getFamily() != userFamily.getFamily())
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);

        User newMember = userRepository.findByNickname(newUserName)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
        log.debug("update shopping list: {}", updateShoppingList);

        updateShoppingList.setName(newName);
        updateShoppingList.setUser(newMember);
        updateShoppingList.setDate(newDate);
        updateShoppingList.setNote(newNote);

        return shoppingListRepository.save(updateShoppingList);
    }

    @Override
    public void deleteShoppingList(Long listId, User user) {
        UserFamily userFamily = userFamilyRepository.findByUser(user)
                .orElseThrow(() -> new BusinessLogicException(MessageKeys.YOU_NOT_CREATE_FAMILY));
        if (!userFamily.getRole().getName().equals(AppConstants.RoleType.LEADER))
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);

        ShoppingList deleteShoppingList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));

        if (deleteShoppingList.getFamily() != userFamily.getFamily())
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);

        shoppingListRepository.delete(deleteShoppingList);
        log.debug("delete shoppign list: {}", deleteShoppingList);
    }

    @Override
    public void createTasksForList(Long listId, List<AddTagRequest> tasks, User user) {
        ShoppingList shoppingList = shoppingListRepository.findById(listId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));

        // neu user khong phai nguoi duoc gan hoac khong phai leader
        if (shoppingList.getUser() != user && shoppingList.getFamily().getLeader() != user)
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);

        List<Task> newTasks = tasks.stream().map(taskRequest -> {
            Food food = foodRepository.findByName(taskRequest.getFoodName())
                    .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
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
        UserFamily userFamily = userFamilyRepository.findByUser(user)
                .orElseThrow(() -> new BusinessLogicException(MessageKeys.YOU_NOT_CREATE_FAMILY));
        return userFamily.getFamily().getShoppingLists();
    }

    @Override
    public void deleteTaskInFamily(Long taskId, User user) {
        UserFamily userFamily = userFamilyRepository.findByUser(user)
                .orElseThrow(() -> new BusinessLogicException(MessageKeys.YOU_NOT_CREATE_FAMILY));
        if (!userFamily.getRole().getName().equals(AppConstants.RoleType.LEADER))
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);

        Task deleteTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));

        if (deleteTask.getShoppingList().getFamily() != userFamily.getFamily())
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);
        taskRepository.delete(deleteTask);
        log.debug("delete task: {}", deleteTask);
    }

    @Override
    public void updateTaskInFamily(Long taskId, String newFoodName, Integer quantity, User user) {
        Task updateTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
        if (updateTask.getShoppingList().getUser() == user || (updateTask.getShoppingList().getFamily() != null
                ? updateTask.getShoppingList().getFamily().getLeader() == user
                : false)) {
            Food newFood = foodRepository.findByName(newFoodName)
                    .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
            log.debug("update task: {}", updateTask);
            updateTask.setFood(newFood);
            updateTask.setQuantity(quantity);
            taskRepository.save(updateTask);
        } else {
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);
        }
    }

    @Override
    public void markTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
        if (task.getShoppingList().getUser() != user)
            throw new BusinessLogicException(MessageKeys.YOU_NOT_HAVE_PERMISSION);
        task.setStatus(TaskStatus.DONE);
        log.debug("mark task done: {}", task);
        taskRepository.save(task);
    }

}
