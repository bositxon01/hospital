package hospital.hospital_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Transactional

@SQLRestriction(value = "deleted=false")
@SQLDelete(sql = ("update employee set deleted=true where id=?"))
public class Employee extends Person {

    private String specialization;

    @OneToOne
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private List<Appointment> appointments;

    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private List<WorkTime> workTimes;

    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    @ToString.Exclude
    private List<EmployeeRoom> employeeRooms;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    private Attachment attachment;


}
