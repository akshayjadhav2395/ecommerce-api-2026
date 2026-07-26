package com.akshay.ecommerce.product.repository;

import com.akshay.ecommerce.product.entity.Product;
import com.akshay.ecommerce.product.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    public boolean existsBySku(String sku);

    public List<Product> findByName(String name);

    public Page<Product> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(
            String name, String brand, Pageable pageable);

    public Page<Product> findByBrandIgnoreCase(String brand, Pageable pageable);

    public Page<Product> findByStatus(ProductStatus status, Pageable pageable);
}
