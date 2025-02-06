package hospital.hospital_system.payload;

import hospital.hospital_system.enums.PermissionEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpDTO {
    @NotBlank(message = "Username is required for sign-up")
    private String username;

    @NotBlank(message = "Password is required for sign-up")
    private String password;

    @NotNull(message = "PositionDTO is required for sign-up")
    private PositionDTO positionDTO;

    @NotNull(message = "EmployeeDTO is required for sign-up")
    private EmployeeDTO employeeDTO;

    @NotNull(message = "Permissions are required for sign-up")
    private List<PermissionEnum> permissions;
}
