package hospital.hospital_system.payload;

import hospital.hospital_system.enums.DayEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkTimeWithIdDTO {
    private Integer employee_id;

    private DayEnum day;

    private Integer turn_id;
}
