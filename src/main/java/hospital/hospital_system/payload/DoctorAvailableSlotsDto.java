package hospital.hospital_system.payload;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorAvailableSlotsDto {
    private Integer employeeId;
    private String employeeName;
    private String specialization;
    private List<String> availableSlots;
}
