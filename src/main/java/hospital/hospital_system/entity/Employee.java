package hospital.hospital_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity

@SQLRestriction(value = "deleted=false")
@SQLDelete(sql = ("UPDATE employee SET deleted=true WHERE id=?"))
public class Employee extends Person {

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    private Attachment attachment;

    @NotBlank(message = "Specialization cannot be blank")
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

}
