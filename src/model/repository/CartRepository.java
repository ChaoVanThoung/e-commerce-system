package model.repository;

import model.entity.CartItems;
import model.entity.Carts;
import utils.DatabaseConfig;

import java.sql.*;
import java.util.*;

public class CartRepository implements Repository<Carts, Integer> {



    @Override
    public Carts save(Carts carts) {
        try (Connection con = DatabaseConfig.getConnection()) {
            String sql = """
                    INSERT INTO carts(user_id, is_active)
                    VALUES (?, ?)
                    """;
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, carts.getUserId());
            preparedStatement.setBoolean(2, carts.getIsActive());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cart created successfully");
                var rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    carts.setId(rs.getInt(1));
                }
                return carts;
            }

        } catch (SQLException e) {
            System.out.println("Error in save carts");
        }
        return null;
    }

    @Override
    public List<Carts> findAll() {
        List<Carts> cartsList = new ArrayList<>();
        try (Connection con = DatabaseConfig.getConnection()) {
            String sql = """
                SELECT c.id, c.user_id, c.is_active,
                       ci.id as item_id, ci.product_id, ci.quantity
                FROM carts c
                LEFT JOIN cart_items ci ON c.id = ci.cart_id
                ORDER BY c.id
                """;
            PreparedStatement pre = con.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();

            Map<Integer, Carts> cartsMap = new HashMap<>();

            while (rs.next()) {
                Integer cartId = rs.getInt("id");

                // Get or create cart
                Carts cart = cartsMap.get(cartId);
                if (cart == null) {
                    cart = new Carts();
                    cart.setId(cartId);
                    cart.setUserId(rs.getInt("user_id"));
                    cart.setIsActive(rs.getBoolean("is_active"));
                    cart.setItems(new ArrayList<>());
                    cartsMap.put(cartId, cart);
                }

                // Add cart item if exists
                if (rs.getObject("item_id") != null) {
                    CartItems item = new CartItems();
                    item.setId(rs.getInt("item_id"));
                    item.setCartId(cartId);
                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    cart.getItems().add(item);
                }
            }

            cartsList.addAll(cartsMap.values());
        } catch (SQLException e) {
            System.err.println("Error in findAll carts: " + e.getMessage());
            e.printStackTrace();
        }
        return cartsList;
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
