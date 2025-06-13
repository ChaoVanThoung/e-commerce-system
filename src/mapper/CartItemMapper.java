package mapper;

import model.dto.cart.CartItemRequestDto;
import model.entity.CartItems;

public class CartItemMapper {

    public static CartItems CartItemMapper(CartItemRequestDto cartItemRequestDto) {
        return CartItems.builder()
                .quantity(cartItemRequestDto.quantity())
                .build();
    }
}
