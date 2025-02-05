package hospital.hospital_system.payload;

import hospital.hospital_system.enums.TurnEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurnDTO {
    private Integer id;
    private TurnEnum name;
    private LocalTime startTime;
    private LocalTime endTime;
}
