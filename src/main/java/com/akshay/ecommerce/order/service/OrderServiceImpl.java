package com.akshay.ecommerce.order.service;

import com.akshay.ecommerce.cart.entities.Cart;
import com.akshay.ecommerce.cart.entities.CartItem;
import com.akshay.ecommerce.cart.repository.CartItemRepository;
import com.akshay.ecommerce.cart.repository.CartRepository;
import com.akshay.ecommerce.common.exception.ResourceNotFoundException;
import com.akshay.ecommerce.order.dto.OrderRequest;
import com.akshay.ecommerce.order.dto.OrderResponse;
import com.akshay.ecommerce.order.entities.Order;
import com.akshay.ecommerce.order.entities.OrderItem;
import com.akshay.ecommerce.order.enums.OrderStatus;
import com.akshay.ecommerce.order.mapper.OrderMapper;
import com.akshay.ecommerce.order.repository.OrderRepository;
import com.akshay.ecommerce.product.entity.Product;
import com.akshay.ecommerce.product.repository.ProductRepository;
import com.akshay.ecommerce.user.entity.User;
import com.akshay.ecommerce.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final CartRepository cartRepository;

    private final OrderMapper orderMapper;

    private final CartItemRepository cartItemRepository;

    public OrderServiceImpl(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository, CartRepository cartRepository, OrderMapper orderMapper,
                            CartItemRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.orderMapper = orderMapper;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {

        //get current logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found : " + email));

        //get cart
        Cart cart = cartRepository.findByUser(user);

        //validate cart
        if(cart == null) {
            throw new ResourceNotFoundException("Cart not found!");
        }

        //validate cartItem
        if(cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        //create orderItems List
        List<OrderItem> orderItems = new ArrayList<>();

        //setTotalAmount
        BigDecimal totalAmount = BigDecimal.ZERO;

        //Create Order

        Order order = Order.builder()
                .user(user)
                .orderStatus(OrderStatus.PENDING)
                .shippingAddress(orderRequest.getShippingAddress())
                .paymentMethod(orderRequest.getPaymentMethod())
                .build();

        //convert cartItem into order
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        for(CartItem cartItem : cartItems) {

            //validate stock /  quantity
            if(cartItem.getQuantity() > cartItem.getProduct().getStockQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product : " + cartItem.getProduct().getName());
            }

            //create orderItem,
            // converting cartItem to orderItem and setInto OrderItemsList
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPrice())
                    .build();

            //set orderItem to OrderItemsList
            orderItems.add(orderItem);

            //calculate subTotal if multiple items or quantity
            BigDecimal subTotal = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            //set totalAmount
            totalAmount = totalAmount.add(subTotal);

            //reduce stock

                //getProduct
            Product product = cartItem.getProduct();

            //stock reduced
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());

            //updated stock and savedProduct
            productRepository.save(product);

        }

        //set remaining field (totalAmount) and orderItems to order
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        //save order
        order = orderRepository.save(order);

        //clear cart after saving/placing order
        cart.getCartItems().clear();

        //save cart after clearing cartitems as empty
        cartRepository.save(cart);

        //return order
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getOrders() {

        //get current logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found : " + email));

        //getPlaced orders
        List<OrderResponse> orderResponseList = orderRepository.findByUser(currentUser).stream().map(order -> orderMapper.toOrderResponse(order)).collect(Collectors.toList());

        return orderResponseList;
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found : " + orderId));

        return orderMapper.toOrderResponse(order);
    }
}
