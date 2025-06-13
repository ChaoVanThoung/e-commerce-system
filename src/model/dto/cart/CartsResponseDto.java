package model.dto.cart;

import java.util.List;

public record CartsResponseDto(
        Integer id,
        Integer user_id,
        Boolean is_active,
        List<CartItemResponse> items,
        Double total_price
) {
}
