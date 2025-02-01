package hospital.hospital_system.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PositionDTO {
    private Integer id;
    private String name;
    private Double salary;
}
