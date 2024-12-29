package com.hust.smart_Shopping.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.smart_Shopping.exceptions.payload.BusinessLogicException;
import com.hust.smart_Shopping.exceptions.payload.DataNotFoundException;
import com.hust.smart_Shopping.models.Category;
import com.hust.smart_Shopping.models.Unit;
import com.hust.smart_Shopping.repositories.CategoryRepository;
import com.hust.smart_Shopping.repositories.UnitRepository;
import com.hust.smart_Shopping.services.AdminService;
import com.hust.smart_Shopping.utils.MessageKeys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final CategoryRepository categoryRepository;
    private final UnitRepository unitRepository;

    @Override
    public Category createCategory(String name) {
        if (categoryRepository.existsByName(name))
            throw new BusinessLogicException(MessageKeys.CATEGORY_EXIST);
        Category newCategory = new Category();
        newCategory.setName(name);
        log.debug("create category : {}", newCategory);
        return categoryRepository.save(newCategory);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void updateCategory(String oldName, String newName) {

        Category updateCategory = categoryRepository.findByName(oldName)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
        updateCategory.setName(newName);
        categoryRepository.save(updateCategory);
        log.debug("update category: {}", updateCategory);
    }

    @Override
    public void deleteCategory(String name) {
        Category deleteCategory = categoryRepository.findByName(name)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
        categoryRepository.delete(deleteCategory);
        log.debug("delete category with name: {}", name);

    }

    @Override
    public Unit createUnit(String name) {
        if (unitRepository.existsByName(name))
            throw new BusinessLogicException(MessageKeys.UNIT_EXIST);
        Unit newUnit = new Unit();
        newUnit.setName(name);
        log.debug("create unit : {}", newUnit);
        return unitRepository.save(newUnit);
    }

    @Override
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    @Override
    public void updateUnit(String oldName, String newName) {
        Unit updateUnit = unitRepository.findByName(oldName)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
        updateUnit.setName(newName);
        unitRepository.save(updateUnit);
        log.debug("update unit: {}", updateUnit);
    }

    @Override
    public void deleteUnit(String name) {
        Unit deleteUnit = unitRepository.findByName(name)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND));
        unitRepository.delete(deleteUnit);
        log.debug("delete unit with name: {}", name);
    }
}
