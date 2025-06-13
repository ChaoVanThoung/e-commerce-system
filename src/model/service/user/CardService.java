package model.service.user;

import model.dto.cart.CartItemRequestDto;
import model.dto.cart.CartItemResponse;
import model.dto.cart.CartsRequestDto;
import model.dto.cart.CartsResponseDto;
import model.dto.product.ProductResponseDto;
import model.entity.CartItems;

import java.util.List;

public interface CardService {
    CartItems addProductToCartByUuid(Integer userId, String uuid, CartItemRequestDto cartItemRequestDto);
    List<CartItemResponse>  findAll();


}
