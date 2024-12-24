package com.hust.smart_Shopping.services.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.smart_Shopping.exceptions.payload.BusinessLogicException;
import com.hust.smart_Shopping.exceptions.payload.DataNotFoundException;
import com.hust.smart_Shopping.models.Food;
import com.hust.smart_Shopping.models.FridgeItem;
import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.repositories.FoodRepository;
import com.hust.smart_Shopping.repositories.FridgeItemRepository;
import com.hust.smart_Shopping.services.FridgeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FridgeServiceImpl implements FridgeService {

    private final FoodRepository foodRepository;
    private final FridgeItemRepository fridgeItemRepository;

    @Override
    public FridgeItem createFridgeItem(String name, Long time, Integer quantity, User user) {
        Food food = foodRepository.findByName(name).orElseThrow(() -> new DataNotFoundException(""));
        FridgeItem newFridgeItem = new FridgeItem();
        newFridgeItem.setFood(food);
        newFridgeItem.setUser(user);
        newFridgeItem.setStartDate(Instant.now());
        newFridgeItem.setExpiredDate(Instant.now().plus(Duration.ofMinutes(time)));
        newFridgeItem.setQuantity(quantity);

        log.debug("create new fridge item: {}", newFridgeItem);
        return fridgeItemRepository.save(newFridgeItem);

    }

    @Override
    public FridgeItem updateFridgeItem(Long id, String newNote, Integer newQuantity, Long newTime, String newName) {
        FridgeItem updateFridgeItem = fridgeItemRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(""));
        Food food = foodRepository.findByName(newName).orElseThrow(() -> new DataNotFoundException(""));

        log.debug("update fridge item: {}", updateFridgeItem);

        updateFridgeItem.setNote(newNote);
        updateFridgeItem.setQuantity(newQuantity);
        updateFridgeItem.setExpiredDate(updateFridgeItem.getStartDate().plus(Duration.ofMinutes(newTime)));
        updateFridgeItem.setFood(food);

        return fridgeItemRepository.save(updateFridgeItem);
    }

    @Override
    public void deleteFridgeItem(String foodName, User user) {
        Food food = foodRepository.findByName(foodName).orElseThrow(() -> new DataNotFoundException(""));

        List<FridgeItem> deleteFridgeItem = fridgeItemRepository.findByFoodAndUser(food, user);

        Optional.ofNullable(deleteFridgeItem)
                .ifPresent(items -> items.forEach(fridgeItem -> {
                    fridgeItemRepository.delete(fridgeItem);
                    log.debug("delete list fridgeItem : {}", fridgeItem);
                }));
    }

    @Override
    public List<FridgeItem> getAllFridgeItemsWithUser(User user) {
        return fridgeItemRepository.findByUser(user);
    }

    @Override
    public List<FridgeItem> getSpecificItem(String foodName) {
        Food food = foodRepository.findByName(foodName).orElseThrow(() -> new DataNotFoundException(""));
        if (!fridgeItemRepository.existsByFood(food))
            throw new BusinessLogicException("");

        return fridgeItemRepository.findByFood(food);
    }

}
