package model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {
    Integer id;
    String username;
    String email;
    String password;
    Boolean isDeleted;
    String userUuid;
}
