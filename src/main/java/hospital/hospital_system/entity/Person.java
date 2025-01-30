package hospital.hospital_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class Person extends AbsIntegerEntity {

    @NotBlank(message = "Firstname cannot be blank")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Lastname cannot be blank")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Date of birth cannot be blank")
    @Past
    @Column(nullable = false)
    private Date dateOfBirth;

}
