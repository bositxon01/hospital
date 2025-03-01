package hospital.hospital_system.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComplaintDTO {

    @NotBlank(message = "Complaint name is required")
    private String name;

    private String description;

}