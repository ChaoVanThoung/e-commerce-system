package controller;

import lombok.AllArgsConstructor;
import model.dto.cart.CartItemRequestDto;
import model.dto.cart.CartItemResponse;
import model.dto.cart.CartsRequestDto;
import model.dto.cart.CartsResponseDto;
import model.dto.product.ProductResponseDto;
import model.entity.CartItems;
import model.repository.CartItemRepositoryImpl;
import model.service.user.CardServiceImpl;

import javax.smartcardio.Card;
import java.util.List;


public class CartController {
    private final CardServiceImpl cardService = new CardServiceImpl();


    public CartItems addProductToCartByUuid(Integer userId ,String uuid, CartItemRequestDto cartItemRequestDto) {
        return cardService.addProductToCartByUuid(userId,uuid, cartItemRequestDto);
    }

    public List<CartItemResponse> findALl(){
        return cardService.findAll();
    }
}
