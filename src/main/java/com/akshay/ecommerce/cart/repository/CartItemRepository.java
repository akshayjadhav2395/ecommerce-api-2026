package com.akshay.ecommerce.cart.repository;

import com.akshay.ecommerce.cart.entities.Cart;
import com.akshay.ecommerce.cart.entities.CartItem;
import com.akshay.ecommerce.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    public CartItem findByCartAndProduct(Cart cart, Product product);

    public List<CartItem> findByCart(Cart cart);

}
