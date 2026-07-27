package com.akshay.ecommerce.category.service;

import com.akshay.ecommerce.category.dto.CategoryRequest;
import com.akshay.ecommerce.category.dto.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    public CategoryResponse createCategory(CategoryRequest categoryRequest);

    public Page<CategoryResponse> getCategories(Pageable pageable);

    public CategoryResponse getCategoryById(Long id);

    public CategoryResponse updateCategory(CategoryRequest categoryRequest, Long id);

    public void deleteCategory(Long id);
}
