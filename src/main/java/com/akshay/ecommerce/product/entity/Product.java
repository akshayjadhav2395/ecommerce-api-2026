package com.akshay.ecommerce.product.entity;

import com.akshay.ecommerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    @Size(max = 150)
    @NotBlank(message = "Product Name is required")
    private String name;

    @Column(length = 150)
    private String description;

    @NotNull(message = "Price is required")
    @Column(nullable = false, precision = 10, scale = 2)
    @Positive(message = "Price should be greater than zero")
    private BigDecimal price;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer stockQuantity;

    @NotBlank
    @Column(nullable = false, unique = true, length = 100)
    private String sku;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String brand;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

}
