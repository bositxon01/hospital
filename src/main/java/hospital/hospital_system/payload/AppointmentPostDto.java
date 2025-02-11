package hospital.hospital_system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentPostDto {
    private Integer patientId;
    private Integer employeeId;
    private Timestamp appointmentTime;
}
