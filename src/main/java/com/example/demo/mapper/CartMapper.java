package com.example.demo.mapper;

import com.example.demo.dto.cartItem.CartItemResponseDto;
import com.example.demo.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "pricePerUnit", source = "product.price")
    @Mapping(target = "totalPrice", expression = "java(calculateTotal(cartItem))")
    CartItemResponseDto toDto(CartItem cartItem);

    default BigDecimal calculateTotal(CartItem cartItem) {
        return cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }
}
