package hospital.hospital_system.payload;

import hospital.hospital_system.aop.ValidEnum;
import hospital.hospital_system.enums.TurnEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurnDTO {

    private Integer id;

    @ValidEnum(enumClass = TurnEnum.class, message = "Invalid turn name")
    private TurnEnum name;

    @NotNull(message = "Start-time is required")
    private LocalTime startTime;

    @NotNull(message = "End-time is required")
    private LocalTime endTime;

}