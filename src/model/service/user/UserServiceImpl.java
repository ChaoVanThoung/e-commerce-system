package model.service.user;

import mapper.UserMapper;
import model.dto.user.UserRequestDto;
import model.entity.Carts;
import model.entity.User;
import model.repository.CartRepository;
import model.repository.UserRepositoryImpl;

import java.io.FileWriter;
import java.io.IOException;


public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepository = new UserRepositoryImpl();
    private final CartRepository cartRepository = new CartRepository();

    @Override
    public User createNewUser(UserRequestDto userRequestDto) {

        // Check if email already exists
        User existingUser = userRepository.findByEmail(userRequestDto.email());
        if (existingUser != null) {
            System.out.println("Email already exists. Cannot create user.");
            return null;
        }

        User user = UserMapper.fromcreateNewUserDtoToUser(userRequestDto);
        User savedUser = userRepository.save(user);

        if (savedUser.getId() != null) {
            Carts carts = new Carts();
            carts.setUserId(savedUser.getId());
            carts.setIsActive(true);
            cartRepository.save(carts);
        }
        return savedUser ;
    }

    @Override
    public Boolean loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        if (user.getPassword() == null || !password.equals(user.getPassword())) {
            return false;
        }
        System.out.println("login successful");

        try(FileWriter writer = new FileWriter("src/session.txt")){
            writer.write(user.getEmail());
        }catch (IOException e){
            System.out.println("Failed to save session.txt" + e.getMessage());
        }
        return true;
    }

}
