package model.service.user;

import lombok.AllArgsConstructor;
import mapper.CartItemMapper;
import mapper.CartMapper;
import model.dto.cart.CartItemRequestDto;
import model.dto.cart.CartItemResponse;
import model.dto.cart.CartsRequestDto;
import model.dto.cart.CartsResponseDto;
import model.entity.CartItems;
import model.entity.Carts;
import model.entity.Product;
import model.entity.User;
import model.repository.CartItemRepositoryImpl;
import model.repository.CartRepository;
import model.repository.ProductRepositoryImpl;
import utils.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CardServiceImpl implements CardService {
    private  ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
    private  CartRepository cartRepository = new CartRepository();
    private  CartItemRepositoryImpl cartItemRepository = new CartItemRepositoryImpl();
    @Override
    public CartItems addProductToCartByUuid(Integer userId, String uuid, CartItemRequestDto cartItemRequestDto) {

        Product product = productRepository.findProductByUuid(uuid);
        if (product == null) {
            System.out.println("Product not found");
            return null;
        }

        Carts activeCarts = cartRepository.findActiveCartByUserId(userId);
        if (activeCarts == null) {
            System.out.println("Active carts not found");
            return null;
        }

        CartItems cartItems = CartItemMapper.CartItemMapper(cartItemRequestDto);
        cartItems.setCartId(activeCarts.getId()); // set correct cart_id from active cart
        cartItems.setProductId(product.getId()); // set correct product_id from product

        CartItems saveCartItem = cartItemRepository.save(cartItems);
        if (saveCartItem == null) {
            System.out.println("Failed to save item to cart");
            return null;
        }

        return saveCartItem;
    }

    @Override
    public List<CartItemResponse> findAll() {
        List<CartItemResponse> cartItemResponseList = new ArrayList<>();
        cartRepository.findAll().forEach(cart -> {
            if (cart.getItems() != null) {
                cart.getItems().forEach(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProductId());
                    if (product != null) {
                        CartItemResponse response = new CartItemResponse(
                                cartItem.getId(),
                                cartItem.getProductId(),
                                product.getPName(),
                                cartItem.getQuantity(),
                                product.getQty(),
                                product.getPrice(),
                                cartItem.getQuantity() * product.getPrice()
                        );
                        cartItemResponseList.add(response);
                    }
                });
            }
        });
        return cartItemResponseList;
    }
}
