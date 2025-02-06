package hospital.hospital_system.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import hospital.hospital_system.aop.ValidEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PatientDTO {
    private Integer id;

    @NotBlank(message = "Firstname is required")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be a past date")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Username is required")
    @Email(message = "Username must be valid email address")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Complaint is required")
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
