package hospital.hospital_system.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeRoomDTO {
    private Integer id;

    private Integer employeeId;

    private Integer roomId;
}
