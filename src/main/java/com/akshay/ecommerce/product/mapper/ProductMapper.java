package com.akshay.ecommerce.product.mapper;

import com.akshay.ecommerce.category.entity.Category;
import com.akshay.ecommerce.product.dto.ProductRequest;
import com.akshay.ecommerce.product.dto.ProductResponse;
import com.akshay.ecommerce.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest productRequest) {

        return Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stockQuantity(productRequest.getStockQuantity())
                .sku(productRequest.getSku())
                .brand(productRequest.getBrand())
                .status(productRequest.getStatus())
                .build();
    }

    public ProductResponse toProductResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .sku(product.getSku())
                .brand(product.getBrand())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .build();
    }
}
