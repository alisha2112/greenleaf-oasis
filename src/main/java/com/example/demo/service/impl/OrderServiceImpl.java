package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.InsufficientStockException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long placeOrder() {
        User user = getCurrentUser();

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException(("Cart is empty"));
        }

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.NEW)
                .createdAt(LocalDateTime.now())
                .totalPrice(java.math.BigDecimal.ZERO)
                .build();

        order = orderRepository.save(order);

        java.math.BigDecimal finalPrice = java.math.BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for product: " + product.getName()
                );
            }

            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .pricePerUnit(product.getPrice())
                    .build();

            orderItemRepository.save(orderItem);

            finalPrice = finalPrice.add(
                    product.getPrice().multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }

        order.setTotalPrice(finalPrice);
        orderRepository.save(order);

        cartItemRepository.deleteByUser(user);

        log.info("Order placed successfully. ID: {}", order.getId());
        return order.getId();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
