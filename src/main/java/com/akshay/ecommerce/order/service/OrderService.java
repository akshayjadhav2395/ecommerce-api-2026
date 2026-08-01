package com.akshay.ecommerce.order.service;

import com.akshay.ecommerce.order.dto.OrderRequest;
import com.akshay.ecommerce.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    public OrderResponse placeOrder(OrderRequest orderRequest);

    public List<OrderResponse> getOrders();

    public OrderResponse getOrderById(Long orderId);
}
