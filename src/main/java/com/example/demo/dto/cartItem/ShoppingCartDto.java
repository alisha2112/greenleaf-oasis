package com.example.demo.dto.cartItem;

import java.math.BigDecimal;
import java.util.List;

public record ShoppingCartDto(
        List<CartItemResponseDto> items,
        BigDecimal totalCartPrice
) {}
