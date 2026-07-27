package com.akshay.ecommerce.category.mapper;

import com.akshay.ecommerce.category.dto.CategoryRequest;
import com.akshay.ecommerce.category.dto.CategoryResponse;
import com.akshay.ecommerce.category.entity.Category;
import com.akshay.ecommerce.product.dto.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest categoryRequest) {

        return Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .build();
    }

    public CategoryResponse toResponse(Category category) {

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
