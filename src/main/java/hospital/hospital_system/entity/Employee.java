package hospital.hospital_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
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
    private Attachment attachment;


    private String specialization;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private List<WorkTime> workTimes;

    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private List<EmployeeRoom> employeeRooms;

}
