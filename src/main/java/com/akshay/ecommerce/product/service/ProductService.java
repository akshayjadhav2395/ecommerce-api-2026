package com.akshay.ecommerce.product.service;

import com.akshay.ecommerce.product.dto.ProductRequest;
import com.akshay.ecommerce.product.dto.ProductResponse;
import com.akshay.ecommerce.product.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ProductService {

    public ProductResponse saveProduct(ProductRequest productRequest);

    public Page<ProductResponse> getAllProducts(Pageable pageable);

    public ProductResponse getProductById(Long id);

    public ProductResponse updateProduct(ProductRequest productRequest, Long id);

    public void deleteProduct(Long id);

    public Page<ProductResponse> searchProduct(String keyword, Pageable pageable);

    public Page<ProductResponse> filterByBrand(String brand, Pageable pageable);

    public Page<ProductResponse> filterByStatus(ProductStatus status, Pageable pageable);

    public Page<ProductResponse> searchProducts(String keyword, String brand, ProductStatus status, Pageable pageable);
}
