package hospital.hospital_system.dto;

import hospital.hospital_system.enums.GenderEnum;
import hospital.hospital_system.enums.Speciality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorDTO {
    private Integer id;

    private String firstName;

    private String lastName;

    private GenderEnum gender;

    private Speciality speciality;

    private Date birthDate;

    private String email;

    private String phoneNumber;

    private String address;
}
