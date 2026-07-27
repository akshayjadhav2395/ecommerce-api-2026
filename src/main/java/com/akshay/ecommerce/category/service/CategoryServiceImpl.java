package com.akshay.ecommerce.category.service;

import com.akshay.ecommerce.category.dto.CategoryRequest;
import com.akshay.ecommerce.category.dto.CategoryResponse;
import com.akshay.ecommerce.category.entity.Category;
import com.akshay.ecommerce.category.mapper.CategoryMapper;
import com.akshay.ecommerce.category.repository.CategoryRepository;
import com.akshay.ecommerce.common.exception.ResourceAlreadyExistsException;
import com.akshay.ecommerce.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {

        if(categoryRepository.existsByNameIgnoreCase(categoryRequest.getName())) {
            throw new ResourceAlreadyExistsException("Category already exist!" + categoryRequest.getName());
        }

        Category category = categoryMapper.toEntity(categoryRequest);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getCategories(Pageable pageable) {

        Page<CategoryResponse> categoryResponses = categoryRepository.findAll(pageable).map(category -> categoryMapper.toResponse(category));

        return categoryResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found : " + id));

        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse updateCategory(CategoryRequest categoryRequest, Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found : " + id));

        Optional<Category> byName = categoryRepository.findByNameIgnoreCase(categoryRequest.getName());

        if(byName.isPresent() && !byName.get().getId().equals(id)) {
            throw new ResourceAlreadyExistsException("Category already exists : " + id + categoryRequest.getName());
        }

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found : " + id));

        categoryRepository.delete(category);

    }
}
