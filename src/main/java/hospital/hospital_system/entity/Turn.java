package hospital.hospital_system.entity;

import hospital.hospital_system.enums.TurnEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Time;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Turn extends AbsIntegerEntity {

    @NotBlank
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private TurnEnum name;

    @NotBlank
    @Column(nullable = false)
    private Time startTime;

    @NotBlank
    @Column(nullable = false)
    private Time endTime;

    @OneToMany(mappedBy = "turn")
    private List<WorkTime> workTimes;
}
