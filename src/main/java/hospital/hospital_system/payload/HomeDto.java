package hospital.hospital_system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HomeDto {
    private String positionLink;
    private String employeeLink;
    private String workTimeLink;
    private String employeeRoomLink;
    private String appointmentLink;
    private EmployeeDTO employeeDto;
    private String attachmentPath;
    private String clinicAttachmentPath;
}
