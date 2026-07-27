package com.akshay.ecommerce.category.controller;

import com.akshay.ecommerce.category.dto.CategoryRequest;
import com.akshay.ecommerce.category.dto.CategoryResponse;
import com.akshay.ecommerce.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories/")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest category) {

        CategoryResponse categoryResponse = categoryService.createCategory(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CategoryResponse> categoryResponse = categoryService.getCategories(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(categoryResponse);

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {

        CategoryResponse category = categoryService.getCategoryById(id);

        return ResponseEntity.status(HttpStatus.OK).body(category);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @RequestBody CategoryRequest categoryRequest,
                                                           @PathVariable Long id) {

        CategoryResponse category = categoryService.updateCategory(categoryRequest, id);

        return ResponseEntity.status(HttpStatus.OK).body(category);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {

            categoryService.deleteCategory(id);

        return ResponseEntity.status(HttpStatus.OK).body("Category deleted successfully! " + id);

    }
}
