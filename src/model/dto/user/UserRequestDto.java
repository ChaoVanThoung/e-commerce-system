package model.dto.user;

import lombok.Builder;

@Builder
public record UserRequestDto(
        String user_name,
        String email,
        String password
) {
}
