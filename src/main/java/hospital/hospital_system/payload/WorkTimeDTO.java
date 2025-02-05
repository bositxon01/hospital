package hospital.hospital_system.payload;

import hospital.hospital_system.enums.DayEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkTimeDTO {
    private EmployeeDTO employeeDTO;
    private DayEnum day;
    private TurnDTO turnDTO;
}
