package com.akshay.ecommerce.cart.mapper;

import com.akshay.ecommerce.cart.dto.CartItemResponse;
import com.akshay.ecommerce.cart.dto.CartRequest;
import com.akshay.ecommerce.cart.dto.CartResponse;
import com.akshay.ecommerce.cart.entities.Cart;
import com.akshay.ecommerce.cart.entities.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartItemResponse toCartItemResponse(CartItem cartItems) {

        return CartItemResponse.builder()
                .id(cartItems.getId())
                .productId(cartItems.getProduct().getId())
                .productName(cartItems.getProduct().getName())
                .quantity(cartItems.getQuantity())
                .price(cartItems.getPrice())
                .subTotal(
                        cartItems.getPrice()
                                .multiply(BigDecimal
                                        .valueOf(cartItems.getQuantity())))
                .build();
    }

    public CartResponse toCartResponse(Cart cart) {

        List<CartItemResponse> cartItemResponse = cart.getCartItems().stream()
                .map(cartItems -> toCartItemResponse(cartItems)).collect(Collectors.toList());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for(CartItemResponse cartItem : cartItemResponse) {
            totalAmount = totalAmount.add(cartItem.getSubTotal());
        }

        return CartResponse.builder()
                .cartId(cart.getId())
                .cartItems(cartItemResponse)
                .totalAmount(totalAmount)
                .build();
    }
}
