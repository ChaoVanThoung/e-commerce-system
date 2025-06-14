package model.service.user;

import exception.ProductNotFoundException;
import mapper.ProductMapper;
import model.dto.product.ProductResponseDto;
import model.entity.Product;
import model.repository.ProductRepositoryImpl;
import utils.DatabaseConfig;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {
    private static final int TOTAL_ROWS = 10_000_000;
    private static final int THREAD_COUNT = 10;
    private static final int ROWS_PER_THREAD = TOTAL_ROWS / THREAD_COUNT;
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

    @Override
    public void saveTenMillionProducts() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        long start = System.nanoTime();

        for (int t = 0; t < THREAD_COUNT; t++) {
            int startId = t * ROWS_PER_THREAD + 1;
            int endId = (t + 1) * ROWS_PER_THREAD;
            int threadId = t + 1;
            String filePath = "products_" + threadId + ".csv";

            new Thread(() -> {
                try {
                    productRepository.writeCSV(startId, endId, threadId, filePath);
                    try (Connection conn = DatabaseConfig.getConnection()) {
                        productRepository.copyCSVToPostgres(filePath, conn);
                    }
                } catch (Exception e) {
                    System.out.println("[!] Thread " + threadId + " error: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        latch.await();
        double elapsed = (System.nanoTime() - start) / 1_000_000_000.0;
        System.out.printf("✅ Inserted 10 million products in %.2f seconds%n", elapsed);
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
