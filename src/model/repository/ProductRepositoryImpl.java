package model.repository;

import model.entity.Product;
import utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductRepositoryImpl implements Repository<Product, Integer> {
    @Override
    public Product save(Product product) {
        return null;
    }

    @Override
    public List<Product> findAll() {
        try (Connection con = DatabaseConfig.getConnection()) {
            String sql = """
                    SELECT * FROM products ORDER BY category
                    """;
            ;
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            List<Product> productList = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setQty(rs.getInt("qty"));
                product.setPrice(rs.getDouble("price"));
                product.setIs_deleted(rs.getBoolean("is_deleted"));
                product.setP_uuid(rs.getString("p_uuid"));
                product.setP_name(rs.getString("p_name"));
                product.setCategory(rs.getString("category"));
                productList.add(product);
            }
            return productList;
        } catch (Exception e) {
            System.err.println("Error connecting to database." + e.getMessage());
        }
        return null;
    }

    @Override
    public Integer delete(Integer id) {
        return 0;
    }

    public List<Product> findProductByName(String name) {
        return findProducts(name, "p_name");
    }

    public List<Product> findProductByCategory(String category) {
        return findProducts(category, "category");
    }

    public List<Product> findProducts(String searchValue, String searchColumn) {
        if (searchValue == null || searchValue.trim().isEmpty()) {
            return Collections.emptyList();
        }
        if (!"p_name".equals(searchColumn) && !"category".equals(searchColumn)) {
            throw new IllegalArgumentException("Invalid search column: " + searchColumn);
        }

        List<Product> products = new ArrayList<>();
        String sql = """
                SELECT * FROM products
                WHERE %s ILIKE ? AND is_deleted = false
                """.formatted(searchColumn);

        try (Connection con = DatabaseConfig.getConnection()) {
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setString(1, searchValue.trim() + "%");
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setP_uuid(rs.getString("p_uuid"));
                    product.setP_name(rs.getString("p_name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setQty(rs.getInt("qty"));
                    product.setCategory(rs.getString("category"));
                    product.setIs_deleted(rs.getBoolean("is_deleted"));
                    products.add(product);
                }
            }
        } catch (SQLException exception) {
            System.err.println("[!] ERROR during querying product from database: " + exception.getMessage());
            throw new RuntimeException("Failed to query products", exception);
        }
        return products;
    }
}
