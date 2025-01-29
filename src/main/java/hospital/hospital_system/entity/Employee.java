package hospital.hospital_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Employee extends Person {
    @OneToOne
    private Login login;

    @OneToOne
    private Attachment attachment;

    @ManyToOne
    private Position position;

    @OneToOne
    private Specialization specialization;

    @OneToMany(mappedBy = "employee")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "employee")
    private List<WorkTime> workTimes;

    @OneToMany(mappedBy = "employee")
    private List<EmployeeRoom> employeeRooms;

}
