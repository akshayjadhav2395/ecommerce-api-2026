package com.akshay.ecommerce.product.dto;

import com.akshay.ecommerce.product.entity.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @Size(max = 150)
    @NotBlank(message = "Product Name is required")
    private String name;

    @Size(max = 150)
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price should be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required!")
    @PositiveOrZero(message = "Stock quantity can not be negative")
    private Integer stockQuantity;

    @NotBlank(message = "SKU is required")
    @Size(max = 100)
    private String sku;

    @NotBlank(message = "Brand is required")
    @Size(max = 100)
    private String brand;

    @NotNull(message = "Status is required")
    private ProductStatus status;

    @NotNull(message = "Category is required")
    private Long categoryId;

}
