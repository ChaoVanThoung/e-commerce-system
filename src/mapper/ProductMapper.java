package mapper;

import model.dto.product.ProductResponseDto;
import model.dto.user.UserRequestDto;
import model.entity.Product;

public class ProductMapper {

    public static ProductResponseDto toProductResponseDto(Product product) {
        return ProductResponseDto.builder()
                .p_uuid(product.getP_uuid())
                .p_name(product.getP_name())
                .qty(product.getQty())
                .price(product.getPrice())
                .category(product.getCategory())
                .build();

    }
}
