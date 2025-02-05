package hospital.hospital_system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDTO {
    public EmployeeDTO(String firstName, String lastName, LocalDate birthDate, String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.specialization = specialization;
    }

    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String specialization;
}
