package com.akshay.ecommerce.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequest {

    @NotNull(message = "Product Id is required")
    private Long productId;

    @Positive(message = "Quantity is required")
    @NotNull(message = "Quantity must be greater than zero")
    private Integer quantity;

}
