package com.akshay.ecommerce.cart.service;

import com.akshay.ecommerce.cart.dto.CartRequest;
import com.akshay.ecommerce.cart.dto.CartResponse;
import com.akshay.ecommerce.cart.dto.UpdateCartItemRequest;
import com.akshay.ecommerce.cart.entities.Cart;
import com.akshay.ecommerce.cart.entities.CartItem;
import com.akshay.ecommerce.cart.mapper.CartMapper;
import com.akshay.ecommerce.cart.repository.CartItemRepository;
import com.akshay.ecommerce.cart.repository.CartRepository;
import com.akshay.ecommerce.common.exception.ResourceNotFoundException;
import com.akshay.ecommerce.product.entity.Product;
import com.akshay.ecommerce.product.repository.ProductRepository;
import com.akshay.ecommerce.user.entity.User;
import com.akshay.ecommerce.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final CartItemRepository cartItemRepository;

    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, CartItemRepository cartItemRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
    }


    @Override
    public CartResponse addProductToCart(CartRequest request) {

        //get logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found :" + email));

        //find product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found : " + request.getProductId()));

        //check/validate quantity
        if(request.getQuantity() > product.getStockQuantity()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock.");
        }

        //find or create cart
        Cart cart = cartRepository.findByUser(user);

        if(cart == null) {
            cart = Cart.builder()
                    .user(user)
                    .build();

            cart = cartRepository.save(cart);
        }

        //check if Product Already Exists in Cart
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);

        //update or create cartItem

        //if present
        if(cartItem != null) {

            int updatedQuantity =  cartItem.getQuantity() + request.getQuantity();

            //throw exception if updated quantity is greater than stock quantity
            if(updatedQuantity > product.getStockQuantity()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock.");
            }

            cartItem.setQuantity(updatedQuantity);
        }
        else {
           cartItem = CartItem.builder()
                    .product(product)
                    .cart(cart)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .build();
        }

        cartItemRepository.save(cartItem);

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse getCart() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found : " + email));

        Cart cart = cartRepository.findByUser(user);

        if(cart == null) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        return cartMapper.toCartResponse(cart);
    }


    @Override
    public CartResponse updateCart(Long cartItemId, UpdateCartItemRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found : " + email));

        Cart cart = cartRepository.findByUser(user);

        if(cart == null) {
            throw new ResourceNotFoundException("Cart not found");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item not found : " + cartItemId));

        if(!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Cart item does not belong to your cart.");
        }

        if(request.getQuantity() > cartItem.getProduct().getStockQuantity()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock.");
        }

        cartItem.setQuantity(request.getQuantity());

        cartItemRepository.save(cartItem);

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public void removeCartItem(Long cartItemId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found.");
        }
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found : " + cartItemId));

        if(!cartItem.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Cart item does not belong to your cart.");
        }

        cartItemRepository.delete(cartItem);
    }

}
