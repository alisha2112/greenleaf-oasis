package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.InsufficientStockException;
import com.example.demo.repository.*;
import com.example.demo.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private SecurityContext securityContext;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void placeOrder_Successful() {
        String userEmail = "test@example.com";
        User user = User.builder().email(userEmail).build();
        user.setId(1L);

        Product product = Product.builder()
                .name("Test")
                .price(BigDecimal.valueOf(100))
                .stockQuantity(10)
                .build();
        product.setId(100L);

        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(2)
                .build();

        when(authentication.getName()).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(cartItemRepository.findByUser(user)).thenReturn(List.of(cartItem));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(123L);
            return order;
        });

        Long orderId = orderService.placeOrder();

        assertEquals(123L, orderId);

        verify(orderRepository, atLeastOnce()).save(any(Order.class));
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));

        assertEquals(8, product.getStockQuantity());
        verify(productRepository, times(1)).save(product);

        verify(cartItemRepository, times(1)).deleteByUser(user);
    }

    @Test
    void placeOrder_InsufficientStock_ShouldThrowException() {
        String userEmail = "test@example.com";
        User user = User.builder().email(userEmail).build();
        user.setId(1L);

        Product product = Product.builder()
                .stockQuantity(1)
                .build();
        product.setId(100L);

        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(5)
                .build();

        when(authentication.getName()).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(cartItemRepository.findByUser(user)).thenReturn(List.of(cartItem));

        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        assertThrows(InsufficientStockException.class, () -> orderService.placeOrder());

        verify(cartItemRepository, never()).deleteByUser(any());
    }
}
