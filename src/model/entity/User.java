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
    String user_name;
    String email;
    String password;
    Boolean is_deleted;
    String u_uuid;
    Boolean id_login;
}
