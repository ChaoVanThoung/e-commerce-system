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
public class Carts {
    Integer id;
    Integer user_id;
    Boolean is_active;
    List<CartItems> items;
}
