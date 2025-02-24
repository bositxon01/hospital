package hospital.hospital_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class EmployeeRoom extends AbsIntegerEntity {

    @ManyToOne
    private Room room;

    @ManyToOne
    private Employee employee;

}
