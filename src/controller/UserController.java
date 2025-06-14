package controller;

import model.dto.user.UserRequestDto;
import model.entity.User;
import model.service.user.UserServiceImpl;

public class UserController {
    private final UserServiceImpl  userService = new UserServiceImpl();

    public User createNewUser(UserRequestDto userRequestDto) {
        return userService.createNewUser(userRequestDto);
    }

    public boolean loginUser(String email, String password) {
         return userService.loginUser(email,password);
    }

    public Integer getUserIdFromSession() {
        return userService.getUserIdFromSession();
    }
}
