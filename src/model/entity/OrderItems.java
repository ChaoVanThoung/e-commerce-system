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
    Integer orderId;
    String pUuid;
    Integer quantity;
    Integer priceAtOrder;
}
