package com.akshay.ecommerce.cart.controller;

import com.akshay.ecommerce.cart.dto.CartRequest;
import com.akshay.ecommerce.cart.dto.CartResponse;
import com.akshay.ecommerce.cart.dto.UpdateCartItemRequest;
import com.akshay.ecommerce.cart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponse> createCart(@Valid @RequestBody CartRequest request) {

        CartResponse cartResponse = cartService.addProductToCart(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponse);
    }

    @GetMapping("/")
    public ResponseEntity<CartResponse> getCart() {

        CartResponse cart = cartService.getCart();

        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateCart(@PathVariable Long cartItemId, @Valid @RequestBody UpdateCartItemRequest request) {

        CartResponse cartResponse = cartService.updateCart(cartItemId, request);

        return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<String> removeCartItemsFromCart(@PathVariable Long cartItemId) {

        cartService.removeCartItem(cartItemId);

        return ResponseEntity.status(HttpStatus.OK).body("CartItems deleted successfully!");
    }

}
