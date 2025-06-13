package model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CartItems {
    Integer id;
    Integer cart_id;
    Integer product_id;
    Integer quantity;
}
