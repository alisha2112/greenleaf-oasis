package com.example.demo.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank (message = "Name can`t be empty")
        String name
) {}
