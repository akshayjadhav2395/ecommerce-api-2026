package com.akshay.ecommerce.cart.repository;

import com.akshay.ecommerce.cart.entities.Cart;
import com.akshay.ecommerce.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    public boolean existsById(Long id);

    public Cart findByUser(User user);

}
