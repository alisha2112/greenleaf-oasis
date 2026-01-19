package com.example.demo.service;

import com.example.demo.dto.cartItem.CartItemRequestDto;
import com.example.demo.dto.cartItem.ShoppingCartDto;
import com.example.demo.entity.User;

public interface CartService {
    ShoppingCartDto addToCart(CartItemRequestDto request);
    ShoppingCartDto getCart();
    ShoppingCartDto removeItem(Long cartItemId);
}
