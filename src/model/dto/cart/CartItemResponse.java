package model.dto.cart;

public record CartItemResponse(
        Integer id,
        Integer product_id,
        Integer quantity,
        Double subtotal
) {}

