package com.example.demo;

import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.cartItem.CartItemRequestDto;
import com.example.demo.entity.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
public class OrderIntegrationTest extends AbstractIntegrationTest{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldRegisterLoginAndCreateOrder() throws Exception {
        Product product = Product.builder()
                .name("Test Plant")
                .description("Test Description")
                .price(BigDecimal.valueOf(250.00))
                .stockQuantity(10)
                .build();
        product = productRepository.save(product);

        RegisterRequest registerRequest = new RegisterRequest(
                "Test", "User", "testuser@gmail.com", "password"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        AuthenticationRequest loginRequest = new AuthenticationRequest(
                "testuser@gmail.com", "password"
        );

        String loginResponse = mockMvc.perform(post("/api/v1/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(loginResponse);
        String token = jsonNode.get("token").asText();

        CartItemRequestDto cartRequest = new CartItemRequestDto(product.getId(), 2);

        mockMvc.perform(post("/api/v1/cart")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/orders")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertEquals(1, orderRepository.count());

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertEquals(8, updatedProduct.getStockQuantity());
    }
}
