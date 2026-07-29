package com.akshay.ecommerce.cart.service;

import com.akshay.ecommerce.cart.dto.CartRequest;
import com.akshay.ecommerce.cart.dto.CartResponse;
import com.akshay.ecommerce.cart.dto.UpdateCartItemRequest;
import com.akshay.ecommerce.cart.entities.Cart;

public interface CartService {

    public CartResponse addProductToCart(CartRequest request);

    public CartResponse getCart();

    public CartResponse updateCart(Long cartItemId, UpdateCartItemRequest request);

    public void removeCartItem(Long cartItemId);
}
