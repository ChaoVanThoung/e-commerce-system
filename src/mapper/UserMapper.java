package mapper;

import model.dto.user.UserRequestDto;
import model.dto.user.UserResponseDto;
import model.entity.User;

import java.util.Random;
import java.util.UUID;

public class UserMapper {
    public static User fromcreateNewUserDtoToUser(UserRequestDto userRequestDto) {
        return User.builder()
                .id(new Random().nextInt(999999))
                .userUuid(UUID.randomUUID().toString())
                .username(userRequestDto.user_name())
                .email(userRequestDto.email())
                .password(userRequestDto.password())
                .isDeleted(false)
                .build();
    }

    public static UserResponseDto UserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .user_name(user.getUsername())
                .email(user.getEmail())
                .build();
    }


}
