package hospital.hospital_system.payload;

import hospital.hospital_system.enums.PermissionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpDTO {
    private String username;
    private String password;
    private PositionDTO positionDTO;
    private EmployeeDTO employeeDTO;
    private List<PermissionEnum> permissions;
}
