package hospital.hospital_system.payload;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeGetDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String specialization;

    private Integer attachmentId;

    private String position;

    private Integer positionId;

    private Double salary;


}
