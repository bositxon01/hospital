package hospital.hospital_system.dto;

import hospital.hospital_system.enums.PermissionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RolePermissionDTO {
    private Integer id;

    private PermissionEnum permission;
}
