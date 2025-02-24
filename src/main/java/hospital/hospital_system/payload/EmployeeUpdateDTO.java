package hospital.hospital_system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeUpdateDTO {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String specialization;
    private Integer positionId;

}
