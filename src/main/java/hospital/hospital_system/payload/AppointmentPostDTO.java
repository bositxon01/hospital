package hospital.hospital_system.payload;

import jakarta.validation.constraints.Future;
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

    @Future(message = "Appointment time should be future date")
    private LocalDateTime appointmentTime;

}