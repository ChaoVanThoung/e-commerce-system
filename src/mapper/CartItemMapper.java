package mapper;

import model.dto.cart.CartItemRequestDto;
import model.dto.cart.CartItemResponse;
import model.dto.user.UserResponseDto;
import model.entity.CartItems;
import model.entity.User;

public class CartItemMapper {

    public static CartItems CartItemMapper(CartItemRequestDto cartItemRequestDto) {
        return CartItems.builder()
                .quantity(cartItemRequestDto.quantity())
                .build();
    }

}
