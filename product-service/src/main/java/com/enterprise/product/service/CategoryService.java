package com.enterprise.product.service;

import com.enterprise.product.dto.request.CreateCategoryRequest;
import com.enterprise.product.dto.request.UpdateCategoryRequest;
import com.enterprise.product.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService
{
    // createCategory() — nayi category create karega.
    public CategoryResponse createCategory(CreateCategoryRequest request);

    // updateCategory() — existing category update karega.
    public CategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request);

    // getCategoryById() — ID se category fetch karega.
    public CategoryResponse getCategoryById(Long categoryId);

    // getAllCategories() — saari categories return karega.
    public List<CategoryResponse> getAllCategory();

}













