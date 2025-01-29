package hospital.hospital_system.entity;

import hospital.hospital_system.enums.DayEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private DayEnum name;


    @OneToMany(mappedBy = "day")
    private List<WorkTime> workTimes;
}
