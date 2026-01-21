package com.example.demo.controller;

import com.example.demo.dto.cartItem.CartItemRequestDto;
import com.example.demo.dto.cartItem.ShoppingCartDto;
import com.example.demo.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ResourceBundle;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Shopping cart operations for the authenticated user")
public class CartController {
    private final CartService cartService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get current user's cart", description = "Retrieves the shopping cart for the currently authenticated user")
    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Access denied (User login required)")
    public ResponseEntity<ShoppingCartDto> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Add item to cart", description = "Adds a product to the user's cart or updates quantity if it already exists")
    @ApiResponse(responseCode = "200", description = "Item added/updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<ShoppingCartDto> addToCart(@Valid @RequestBody CartItemRequestDto dto) {
        return ResponseEntity.ok(cartService.addToCart(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Remove item from cart", description = "Removes a specific item from the cart by CartItem ID")
    @ApiResponse(responseCode = "200", description = "Item removed successfully")
    @ApiResponse(responseCode = "404", description = "Cart item not found")
    public ResponseEntity<ShoppingCartDto> removeItem(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.removeItem(id));
    }
}
