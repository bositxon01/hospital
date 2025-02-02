package hospital.hospital_system.payload;

import hospital.hospital_system.enums.RoomEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomDTO {
    private Integer id;
    private RoomEnum name;
    private boolean status;
}
