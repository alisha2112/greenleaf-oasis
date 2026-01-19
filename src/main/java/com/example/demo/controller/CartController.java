package com.example.demo.controller;

import com.example.demo.dto.cartItem.CartItemRequestDto;
import com.example.demo.dto.cartItem.ShoppingCartDto;
import com.example.demo.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ResourceBundle;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ShoppingCartDto> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ShoppingCartDto> addToCart(@Valid @RequestBody CartItemRequestDto dto) {
        return ResponseEntity.ok(cartService.addToCart(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ShoppingCartDto> removeItem(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.removeItem(id));
    }
}
