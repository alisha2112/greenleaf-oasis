package com.example.demo.dto.cartItem;

import java.math.BigDecimal;

public record CartItemResponseDto(
   Long id,
   Long productId,
   String productName,
   Integer quantity,
   BigDecimal pricePerUnit,
   BigDecimal totalPrice
) {}
