package hospital.hospital_system.entity;

import hospital.hospital_system.enums.GenderEnum;
import hospital.hospital_system.enums.Speciality;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Firstname cannot be null")
    private String firstName;

    @NotBlank(message = "Lastname cannot be null")
    private String lastName;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @NotBlank
    private Speciality speciality;

    @Past(message = "Birthdate should be past date")
    private Date birthDate;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be in valid form")
    private String email;

    @NotNull
    @Pattern(regexp = "\\+998\\d{9}", message = "Phone number must start with +998 and be followed by 9 digits")
    private String phoneNumber;

    @NotNull(message = "Address cannot be null")
    private String address;
}
