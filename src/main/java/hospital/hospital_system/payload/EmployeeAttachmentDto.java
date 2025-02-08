package hospital.hospital_system.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeAttachmentDto {
    @NotNull(message = "Employee id can not be null")
    private Integer employeeId;
    @NotNull(message = "Attachment id can not be null")
    private Integer attachmentId;
}
