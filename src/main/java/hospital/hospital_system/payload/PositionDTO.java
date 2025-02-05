package hospital.hospital_system.payload;

import hospital.hospital_system.enums.PermissionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PositionDTO {
    private Integer id;
    private String name;
    private Double salary;

    private List<PermissionEnum> permissions;


    public void setName(String name) {
        this.name = name.toUpperCase();
    }
}
