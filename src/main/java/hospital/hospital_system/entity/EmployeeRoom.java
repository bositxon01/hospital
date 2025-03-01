package hospital.hospital_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
@SQLDelete(sql = ("UPDATE employee_room SET deleted=true WHERE id=?"))
public class EmployeeRoom extends AbsIntegerEntity {

    @ManyToOne
    private Room room;

    @ManyToOne
    private Employee employee;

}