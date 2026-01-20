package com.example.demo.service;

import com.example.demo.dto.product.ProductRequestDto;
import com.example.demo.dto.product.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto dto);
    Page<ProductResponseDto> getAllProducts(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            String search,
            Pageable pageable);
    ProductResponseDto getProductById(Long id);
    void deleteProduct(Long id);
}
