package model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderItems {
    Integer id;
    Integer order_id;
    String p_uuid;
    Integer quantity;
    Integer price_at_order;
}
