package model.dto.cart;

import lombok.Builder;

import java.util.List;

@Builder
public record CartsRequestDto (
         Integer user_id,
         Boolean is_active,
         List<CartItemResponse> items
) {
}
