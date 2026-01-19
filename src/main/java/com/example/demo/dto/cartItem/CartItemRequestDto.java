package com.example.demo.dto.cartItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(
        @NotNull
        Long productId,

        @Min(1)
        Integer quantity
) {}
