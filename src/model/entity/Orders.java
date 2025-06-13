package model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Orders {
    Integer id;
    Integer user_id;
    Integer order_code;
    Double total_price;
    List<OrderItems> items;
}
