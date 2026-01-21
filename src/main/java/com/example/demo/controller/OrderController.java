package com.example.demo.controller;

import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order processing and management")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(
            summary = "Place an order",
            description = "Converts the current user's shopping cart into an order and clears the cart."
    )
    @ApiResponse(responseCode = "200", description = "Order placed successfully")
    @ApiResponse(responseCode = "400", description = "Cart is empty")
    @ApiResponse(responseCode = "403", description = "Access denied")
    public ResponseEntity<String> placeOrder() {
        Long orderId = orderService.placeOrder();
        return ResponseEntity.ok("Order placed successfully. Order ID: " + orderId);
    }
}
