package hospital.hospital_system.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private List<String> appointmentIds;


    public <R> PatientDTO(Integer id, @NotBlank(message = "Firstname cannot be blank") String firstName, @NotBlank(message = "Lastname cannot be blank") String lastName, @Past LocalDate dateOfBirth, Integer integer, Integer integer1, R collect) {
    }
}
