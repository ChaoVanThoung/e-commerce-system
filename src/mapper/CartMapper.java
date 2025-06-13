package mapper;

import model.dto.cart.CartsRequestDto;
import model.entity.Carts;

import java.util.ArrayList;
import java.util.List;

public class CartMapper {
    public static Carts fromCreateCartDtoToCart(CartsRequestDto cartRequestDto) {
        return Carts.builder()
                .userId(cartRequestDto.user_id())
                .items(new ArrayList<>())
                .isActive(cartRequestDto.is_active())
                .build();
    }
}
