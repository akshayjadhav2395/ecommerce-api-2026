package com.akshay.ecommerce.category.entity;

import com.akshay.ecommerce.common.entity.BaseEntity;
import com.akshay.ecommerce.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "categories")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150, unique = true)
    @NotBlank(message = "Category Name is required")
    @Size(max = 150)
    private String name;

    @Column(length = 200)
    @Size(max = 200, message = "Description cannot exceed 255 characters")
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> productList = new ArrayList<>();
}
