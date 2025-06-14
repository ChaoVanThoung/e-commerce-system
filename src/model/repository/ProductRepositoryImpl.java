package model.repository;

import model.dto.product.ProductResponseDto;
import model.entity.Product;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import utils.DatabaseConfig;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
                product.setIsDeleted(rs.getBoolean("is_deleted"));
                product.setPUuid(rs.getString("p_uuid"));
                product.setPName(rs.getString("p_name"));
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
                    product.setPUuid(rs.getString("p_uuid"));
                    product.setPName(rs.getString("p_name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setQty(rs.getInt("qty"));
                    product.setCategory(rs.getString("category"));
                    product.setIsDeleted(rs.getBoolean("is_deleted"));
                    products.add(product);
                }
            }
        } catch (SQLException exception) {
            System.err.println("[!] ERROR during querying product from database: " + exception.getMessage());
            throw new RuntimeException("Failed to query products", exception);
        }
        return products;
    }

    public Product findProductByUuid(String uuid) {
        String sql = """
                SELECT * FROM products
                WHERE p_uuid = ?
        """;
        try(Connection con = DatabaseConfig.getConnection()){
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setString(1, uuid);
            ResultSet rs = pre.executeQuery();
            Product product = new Product();
            while (rs.next()) {
                product.setId(rs.getInt("id"));
                product.setPUuid(rs.getString("p_uuid"));
                product.setPName(rs.getString("p_name"));
                product.setPrice(rs.getDouble("price"));
                product.setQty(rs.getInt("qty"));
                product.setCategory(rs.getString("category"));
                product.setIsDeleted(rs.getBoolean("is_deleted"));
            }
            return product;
        }catch (SQLException exception){
            System.err.println("[!] ERROR during querying product from database: " + exception.getMessage());
        }
        return null;
    }

    public Product findById(Integer productId) {
        try (Connection con = DatabaseConfig.getConnection()) {
            String sql = """
                SELECT * FROM products 
                WHERE id = ? AND is_deleted = false
                """;
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet resultSet = pre.executeQuery();

            if (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setPName(resultSet.getString("p_name"));
                product.setPrice(resultSet.getDouble("price"));
                product.setQty(resultSet.getInt("qty"));
                product.setIsDeleted(resultSet.getBoolean("is_deleted"));
                product.setPUuid(resultSet.getString("p_uuid"));
                product.setCategory(resultSet.getString("category"));
                return product;
            }
        } catch (SQLException e) {
            System.err.println("Error in findById Product: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void writeCSV(int start, int end, int threadId, String filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (int i = start; i <= end; i++) {
                String uuid = UUID.randomUUID().toString();
                String name = "P_" + i;
                double price = (i % 1000) + 0.99;
                int qty = i % 500;
                String category = "C_" + (i % 10);
                writer.write(String.format("%s,%.2f,%d,false,%s,%s\n", name, price, qty, uuid, category));
            }
        }
    }

    public void copyCSVToPostgres(String filePath, Connection conn) throws Exception {
        CopyManager copyManager = new CopyManager((BaseConnection) conn);
        try (Reader reader = new FileReader(filePath)) {
            copyManager.copyIn("COPY products (p_name, price, qty, is_deleted, p_uuid, category) FROM STDIN WITH (FORMAT csv)", reader);
        }
    }
}
