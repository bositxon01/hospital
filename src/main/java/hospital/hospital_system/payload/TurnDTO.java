package hospital.hospital_system.payload;

import hospital.hospital_system.enums.TurnEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurnDTO {
    private int id;

    @NotNull(message = "Turn name is required")
    private TurnEnum name;

    @NotNull(message = "Start-time is required for Turn")
    private LocalTime startTime;

    @NotNull(message = "End-time is required for Turn")
    private LocalTime endTime;
}
