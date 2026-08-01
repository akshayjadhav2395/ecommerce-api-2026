package com.akshay.ecommerce.order.mapper;

import com.akshay.ecommerce.order.dto.OrderItemResponse;
import com.akshay.ecommerce.order.dto.OrderRequest;
import com.akshay.ecommerce.order.dto.OrderResponse;
import com.akshay.ecommerce.order.entities.Order;
import com.akshay.ecommerce.order.entities.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order) {

        List<OrderItemResponse> items = order.getOrderItems()
                .stream()
                .map(orderItem -> toOrderItemResponse(orderItem))
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .orderDate(order.getOrderDate())
                .items(items)
                .build();
    }

    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {

        return OrderItemResponse.builder()
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .subTotal(orderItem
                        .getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .build();
    }
}
