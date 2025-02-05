package hospital.hospital_system.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PatientDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String username;
    private String password;
    private ComplaintDTO complaintDTO;

    public PatientDTO(Integer id, String firstName, String lastName, LocalDate dateOfBirth, String username, ComplaintDTO complaintDTO) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.complaintDTO = complaintDTO;
    }
}
