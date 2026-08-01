package com.akshay.ecommerce.order.dto;

import com.akshay.ecommerce.order.entities.OrderItem;
import com.akshay.ecommerce.order.enums.OrderStatus;
import com.akshay.ecommerce.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private OrderStatus orderStatus;

    private BigDecimal totalAmount;

    private LocalDateTime orderDate;

    private String shippingAddress;

    private String paymentMethod;

    private List<OrderItemResponse> items;

}
