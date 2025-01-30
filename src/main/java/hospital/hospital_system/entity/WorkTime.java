package hospital.hospital_system.entity;

import hospital.hospital_system.enums.DayEnum;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class WorkTime extends AbsIntegerEntity {
    @ManyToOne
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayEnum day;

    @ManyToOne
    private Turn turn;


}
