package hospital.hospital_system.entity;

import hospital.hospital_system.enums.BloodGroup;
import hospital.hospital_system.enums.GenderEnum;
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
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Firstname cannot be null")
    private String firstName;

    @NotBlank(message = "Lastname cannot be null")
    private String lastName;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Past(message = "Birthdate should be past date")
    private Date birthDate;

    @NotBlank
    private BloodGroup bloodGroup;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be in valid form")
    private String email;

    @NotNull
    @Pattern(regexp = "\\+998\\d{9}", message = "Phone number must start with +998 and be followed by 9 digits")
    private String phoneNumber;

    @NotNull(message = "Address cannot be null")
    private String address;
}
