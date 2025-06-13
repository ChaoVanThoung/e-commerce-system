package model.dto.user;

import lombok.Builder;

@Builder
public record UserResponseDto(
        Integer id,
        String user_name,
        String email,
        String password,
        Boolean is_deleted,
        String u_uuid,
        Boolean id_login
) {
}
