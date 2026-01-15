package com.example.demo.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDto (
        @NotBlank (message = "Name can`t be empty")
        String name,

        @Size (max = 1000, message = "Description must be no longer than 1000 characters")
        String description,

        @NotNull (message = "Price is mandatory")
        @Positive (message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull (message = "Stock quantity is mandatory")
        @PositiveOrZero (message = "Stock quantity can`t be less than 0")
        Integer stockQuantity,

        @NotNull (message = "ID of category is mandatory")
        Long categoryId
        ) {}
