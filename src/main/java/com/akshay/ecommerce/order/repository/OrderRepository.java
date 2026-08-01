package com.akshay.ecommerce.order.repository;

import com.akshay.ecommerce.order.entities.Order;
import com.akshay.ecommerce.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findByUser(User user);

}
