package hospital.hospital_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@MappedSuperclass
@FieldNameConstants
public class Person extends AbsIntegerEntity {

    @NotBlank(message = "Firstname cannot be blank")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Lastname cannot be blank")
    @Column(nullable = false)
    private String lastName;

    @Past
    @Column(nullable = false)
    private LocalDate dateOfBirth;

}
