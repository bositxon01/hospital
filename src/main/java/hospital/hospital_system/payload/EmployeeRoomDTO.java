package hospital.hospital_system.payload;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldNameConstants
public class EmployeeRoomDTO {

    private Integer id;

    private Integer employeeId;

    private Integer roomId;

}