package model.dto.user;

import lombok.Builder;
import lombok.Data;

@Builder
public record UserRequestDto(
        String user_name,
        String email,
        String password
) {
}
