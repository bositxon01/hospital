package hospital.hospital_system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentGetDTO {
    private Integer id;
    private Integer employeeId;
    private Integer patientId;
    private Integer roomId;
    private Timestamp appointmentTime;
    private Integer appointmentResultId;
}
