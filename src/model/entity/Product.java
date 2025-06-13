package model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Product {
    Integer id;
    String pName;
    double price;
    Integer qty;
    Boolean isDeleted;
    String pUuid;
    String category;
}
