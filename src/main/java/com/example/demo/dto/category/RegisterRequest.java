package com.example.demo.dto.category;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest (
        @NotBlank (message = "Name is mandatory")
        String firstName,

        @NotBlank (message = "Last name is mandatory")
        String lastName,

        @Email (message = "Incorrect email format")
        @NotBlank(message = "Email is mandatory")
        String email,

        @NotBlank (message = "Password is mandatory")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password
) {}
