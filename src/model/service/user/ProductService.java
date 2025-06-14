package model.service.user;

import model.dto.product.ProductResponseDto;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> findAll();
    List<ProductResponseDto> findProductByName(String name);
    List<ProductResponseDto> findProductByCategory(String category);

    void saveTenMillionProducts() throws InterruptedException;
}
