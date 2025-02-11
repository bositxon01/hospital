package hospital.hospital_system.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeFilterDTO {

    private String firstName;

    private String lastName;

    private String specialization;

    private String positionName;

    private LocalDate birthDateFrom;

    private LocalDate birthDateTo;
}
