package com.hust.smart_Shopping.services;

import java.util.List;

import com.hust.smart_Shopping.models.Category;
import com.hust.smart_Shopping.models.Unit;

public interface AdminService {
    Category createCategory(String categoryName);

    List<Category> getAllCategories();

    void updateCategory(String oldName, String newName);

    void deleteCategory(String name);

    Unit createUnit(String unitName);

    List<Unit> getAllUnits();

    void updateUnit(String oldName, String newName);

    void deleteUnit(String name);
}
