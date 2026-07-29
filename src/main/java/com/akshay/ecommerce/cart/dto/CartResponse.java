package com.akshay.ecommerce.cart.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {

    private Long cartId;

    private List<CartItemResponse> cartItems = new ArrayList<>();

    private BigDecimal totalAmount;

}
