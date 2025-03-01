package hospital.hospital_system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentResultDTO {

    private Integer id;

    private AppointmentGetDTO appointment;

    private String diagnosis;

    private String medicine;

    private AppointmentGetDTO nextAppointment;

}