package com.akshay.ecommerce.order.dto;

import com.akshay.ecommerce.order.enums.OrderStatus;
import com.akshay.ecommerce.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

}
