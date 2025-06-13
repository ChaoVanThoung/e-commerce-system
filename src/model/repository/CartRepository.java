package model.repository;

import model.entity.Carts;
import utils.DatabaseConfig;

import java.sql.*;
import java.util.List;

public class CartRepository implements Repository<Carts,Integer> {
    @Override
    public Carts save(Carts carts) {
        try (Connection con = DatabaseConfig.getConnection()) {
            String sql = """
                    INSERT INTO carts(user_id, is_active)
                    VALUES (?, ?)
                    """;
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,carts.getUserId());
            preparedStatement.setBoolean(2,carts.getIsActive());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cart created successfully");
                var rs  = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    carts.setId(rs.getInt(1));
                }
            }
                return carts;
        } catch (SQLException e){
            System.out.println("Error in save carts");
        }
        return null;
    }

    @Override
    public List<Carts> findAll() {
        return List.of();
    }

    @Override
    public Integer delete(Integer id) {
        return 0;
    }

    public Carts findActiveCartByUserId(Integer userId) {
        try (Connection con = DatabaseConfig.getConnection()) {
            String sql = "SELECT * FROM carts WHERE user_id = ? AND is_active = true";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Carts.builder()
                        .id(rs.getInt("id"))
                        .userId(rs.getInt("user_id"))
                        .isActive(rs.getBoolean("is_active"))
                        .build();
            }
        } catch (SQLException e) {
            System.out.println("Error finding active cart: " + e.getMessage());
        }
        return null;
    }
}
