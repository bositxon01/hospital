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

    private EmployeeDTO employee;

    private PatientDTO patient;

    private RoomDTO room;

    private Timestamp appointmentTime;

    private AppointmentResultDTO appointmentResultId;

}