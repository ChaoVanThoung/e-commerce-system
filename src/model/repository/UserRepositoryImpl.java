package model.repository;

import model.entity.User;
import utils.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class UserRepositoryImpl implements Repository<User,Integer>{
    @Override
    public User save(User user) {
        try(Connection con = DatabaseConfig.getConnection()){
            String sql = """
                    INSERT INTO users(user_name,email,password,is_deleted,u_uuid)
                    VALUES(?,?,?,?,?)
                    """;
            PreparedStatement pre = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pre.setString(1, user.getUsername());
            pre.setString(2, user.getEmail());
            pre.setString(3, user.getPassword());
            pre.setBoolean(4, user.getIsDeleted());
            pre.setString(5, user.getUserUuid());
            int rowAffected = pre.executeUpdate();
            if(rowAffected > 0){
                System.out.println("User has been saved successfully");
                var rs = pre.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                }
                return user;
            }
        }catch (Exception e){
            System.err.println("Error Database Connection" + e.getMessage());
        }
        return user;
    }

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Integer delete(Integer id) {
        return 0;
    }

    public User findByEmail(String email) {
        try(Connection con = DatabaseConfig.getConnection()){
            String  sql = """
                    SELECT * FROM users 
                    WHERE email = ?;
            """;
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setString(1, email);
            ResultSet resultSet = pre.executeQuery();
            User user = new User();
            if(resultSet.next()){
                user.setUsername(resultSet.getString("user_name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setIsDeleted(resultSet.getBoolean("is_deleted"));
                user.setUserUuid(resultSet.getString("u_uuid"));
            }
            return user;
        }catch (Exception e){
            System.err.println("Error Database Connection" + e.getMessage());
        }
        return null;
    }

}
