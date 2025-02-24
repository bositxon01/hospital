package hospital.hospital_system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentPostDTO {
    private Integer patientId;
    private Integer employeeId;
    private LocalDateTime appointmentTime;
}
