package com.example.demo.service.impl;

import com.example.demo.dto.cartItem.CartItemRequestDto;
import com.example.demo.dto.cartItem.ShoppingCartDto;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CartMapper;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public ShoppingCartDto addToCart(CartItemRequestDto request) {
        User user = getCurrentUser();
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getStockQuantity() < request.quantity()) {
            throw new RuntimeException("Not enough stock for product: " + product.getName());
        }

        CartItem cartItem = cartItemRepository.findByUserAndProductId(user, product.getId())
                .orElse(CartItem.builder()
                        .user(user)
                        .product(product)
                        .quantity(0)
                        .build());

        cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
        cartItemRepository.save(cartItem);

        return getCart();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public ShoppingCartDto getCart() {
        User user = getCurrentUser();
        List<CartItem> items = cartItemRepository.findByUser(user);

        var itemDtos = items.stream().map(cartMapper::toDto).toList();

        BigDecimal total = itemDtos.stream()
                .map(dto -> dto.totalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ShoppingCartDto(itemDtos, total);
    }

    @Override
    @Transactional
    public ShoppingCartDto removeItem(Long cartItemId) {
        User user = getCurrentUser();
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        cartItemRepository.delete(item);

        return getCart();
    }
}
