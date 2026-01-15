package com.example.demo.dto.product;

import java.math.BigDecimal;

public record ProductResponseDto (
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String categoryName
){}
