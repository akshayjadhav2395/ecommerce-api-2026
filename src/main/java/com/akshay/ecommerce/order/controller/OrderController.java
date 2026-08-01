package com.akshay.ecommerce.order.controller;

import com.akshay.ecommerce.order.dto.OrderRequest;
import com.akshay.ecommerce.order.dto.OrderResponse;
import com.akshay.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest orderRequest) {

        OrderResponse orderResponse = orderService.placeOrder(orderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getOrders() {

        List<OrderResponse> orders = orderService.getOrders();

        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {

        OrderResponse order = orderService.getOrderById(id);

        return ResponseEntity.status(HttpStatus.OK).body(order);
    }
}
