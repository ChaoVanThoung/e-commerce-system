package mapper;

import model.dto.user.UserRequestDto;
import model.entity.User;

import java.util.Random;
import java.util.UUID;

public class UserMapper {
    public static User fromcreateNewUserDtoToUser(UserRequestDto userRequestDto) {
        return User.builder()
                .id(new Random().nextInt(999999))
                .u_uuid(UUID.randomUUID().toString())
                .user_name(userRequestDto.user_name())
                .email(userRequestDto.email())
                .password(userRequestDto.password())
                .is_deleted(false)
                .build();
    }



}
