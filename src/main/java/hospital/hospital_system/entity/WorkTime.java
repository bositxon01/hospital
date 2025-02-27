package hospital.hospital_system.entity;

import hospital.hospital_system.enums.DayEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity

@SQLRestriction(value = "deleted=false")
@SQLDelete(sql = ("UPDATE work_time SET deleted=true WHERE id=?"))
public class WorkTime extends AbsIntegerEntity {

    @ManyToOne
    private Employee employee;

    @NotNull(message = "Day cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayEnum day;

    @ManyToOne
    private Turn turn;

}