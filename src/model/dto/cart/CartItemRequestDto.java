package model.dto.cart;

import lombok.Builder;

@Builder
public record CartItemRequestDto(
        Integer user_id,
        Integer cart_id,
        Integer product_id,
        Integer quantity
) {
}
