package model.service.user;

import model.dto.user.UserRequestDto;
import model.entity.User;

public interface UserService {
    User createNewUser(UserRequestDto userRequestDto);
    Boolean loginUser(String email,String password);
    Integer getUserIdFromSession();
}
