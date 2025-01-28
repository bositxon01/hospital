package hospital.hospital_system.dto;

import hospital.hospital_system.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private Role role;

    private boolean verified;
}
