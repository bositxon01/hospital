package hospital.hospital_system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeAvailableSlotsDTO {

    private Integer employeeId;

    private String employeeName;

    private String specialization;

    private List<String> availableSlots;

}