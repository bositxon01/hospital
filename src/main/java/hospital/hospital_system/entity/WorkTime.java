package hospital.hospital_system.entity;

import hospital.hospital_system.enums.DayEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
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
    private DayEnum day;

    @ManyToOne
    private Turn turn;


}
