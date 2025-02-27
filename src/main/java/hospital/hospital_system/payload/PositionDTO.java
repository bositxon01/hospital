package hospital.hospital_system.payload;

import hospital.hospital_system.enums.PermissionEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldNameConstants
public class PositionDTO {

    private Integer id;

    @NotBlank(message = "Position name is required")
    private String name;

    @NotNull(message = "Salary is required")
    private Double salary;

    private List<PermissionEnum> permissions;

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

}