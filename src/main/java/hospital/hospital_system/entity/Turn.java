package hospital.hospital_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hospital.hospital_system.enums.TurnEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Turn extends AbsIntegerEntity {

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "TurnEnum cannot be null")
    private TurnEnum name;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @OneToMany(mappedBy = "turn")
    @ToString.Exclude
    @JsonIgnore
    private List<WorkTime> workTimes;
}
