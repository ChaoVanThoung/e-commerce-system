package model.service.user;

import exception.ProductNotFoundException;
import mapper.ProductMapper;
import model.dto.product.ProductResponseDto;
import model.entity.Product;
import model.repository.ProductRepositoryImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {
    ProductRepositoryImpl productRepository = new ProductRepositoryImpl();

    @Override
    public List<ProductResponseDto> findAll() {
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        productRepository.findAll().forEach(p -> productResponseDtoList.add(ProductMapper.toProductResponseDto(p)));
        return productResponseDtoList;
    }

    @Override
    public List<ProductResponseDto> findProductByName(String name) {
        try {
            validateInput(name, "Product name cannot be empty");
            return findProducts(productRepository.findProductByName(name));
        } catch (IllegalArgumentException | ProductNotFoundException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<ProductResponseDto> findProductByCategory(String category) {
        try {
            validateInput(category, "Category cannot be empty");
            return findProducts(productRepository.findProductByCategory(category));
        } catch (IllegalArgumentException | ProductNotFoundException e) {
            return Collections.emptyList();
        }
    }

    private void validateInput(String input, String errorMessage) {
        Optional.ofNullable(input)
                .filter(s -> !s.trim().isEmpty())
                .orElseThrow(() -> new IllegalArgumentException(errorMessage));
    }

    private List<ProductResponseDto> findProducts(List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new ProductNotFoundException("No products found");
        }
        return products.stream()
                .map(ProductMapper::toProductResponseDto)
                .collect(Collectors.toList());
    }
}
