package com.akshay.ecommerce.category.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank(message = "Category Name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 200)
    private String description;

}
