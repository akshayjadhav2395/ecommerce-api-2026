package com.akshay.ecommerce.category.repository;

import com.akshay.ecommerce.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Optional<Category> findByNameIgnoreCase(String name);

    public boolean existsByNameIgnoreCase(String name);
}
