package model.dto.product;

import lombok.Builder;

@Builder
public record ProductResponseDto(
        String p_uuid,
        String p_name,
        double price,
        Integer qty,
        String category

) {
}
