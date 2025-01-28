package hospital.hospital_system.dto;

import hospital.hospital_system.enums.BloodGroup;
import hospital.hospital_system.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientDTO {
    private Integer id;

    private String firstName;

    private String lastName;

    private GenderEnum gender;

    private Date birthDate;

    private BloodGroup bloodGroup;

    private String email;

    private String phoneNumber;

    private String address;
}
