package controller;

import model.dto.product.ProductResponseDto;
import model.service.user.ProductServiceImpl;

import java.util.List;

public class ProductController {
    ProductServiceImpl  productService = new ProductServiceImpl();

    public List<ProductResponseDto> findALl(){
        return productService.findAll();
    }

    public List<ProductResponseDto> findProductByName(String name) {
        return productService.findProductByName(name);
    }

    public List<ProductResponseDto> findProductByCategory(String category) {
        return productService.findProductByCategory(category);
    }

    public void saveTenMillionRecords() throws InterruptedException {
        productService.saveTenMillionProducts();
    }
}
