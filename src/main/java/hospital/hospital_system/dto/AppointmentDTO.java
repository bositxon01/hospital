package hospital.hospital_system.dto;

import hospital.hospital_system.entity.Doctor;
import hospital.hospital_system.entity.Patient;
import hospital.hospital_system.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentDTO {

    private Integer id;

    private Doctor doctor;

    private Patient patient;

    private Timestamp appointmentTime;

    private AppointmentStatus status;
}
