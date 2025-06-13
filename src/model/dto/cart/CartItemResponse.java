package model.dto.cart;

import lombok.Builder;

@Builder
public record CartItemResponse(
        Integer id,
        Integer product_id,
        String product_name,
        Integer quantity,
        Integer product_qty,
        Double price,
        Double subtotal
) {}

