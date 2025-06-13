package model.repository;

import model.entity.CartItems;
import utils.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CartItemRepositoryImpl implements Repository<CartItems, Integer> {
    @Override
    public CartItems save(CartItems cartItems) {
        try (Connection con = DatabaseConfig.getConnection()) {
            String sql = """
                    INSERT INTO cart_items (cart_id, product_id, quantity)
                    VALUES (?, ?, ?)
                    """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,cartItems.getCart_id());
            ps.setInt(2,cartItems.getProduct_id());
            ps.setInt(3,cartItems.getQuantity());
            int rowAffected = ps.executeUpdate();
            if(rowAffected > 0){
                System.out.println("Add Item successfully!");
                var rs = ps.getGeneratedKeys();
                if(rs.next()){
                    cartItems.setId(rs.getInt(1));
                }
                return cartItems;
            }

        } catch (SQLException exception) {
            System.err.println("[!] ERROR during saving cartItems: " + exception.getMessage());
        }
        return null;
    }

    @Override
    public List<CartItems> findAll() {
        return List.of();
    }

    @Override
    public Integer delete(Integer id) {
        return 0;
    }
}
