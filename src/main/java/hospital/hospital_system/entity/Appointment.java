package hospital.hospital_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"appointment_time", "employee_id"}),
                @UniqueConstraint(columnNames = {"room_id", "appointment_time"})
        }
)

@SQLRestriction(value = "deleted=false")
@SQLDelete(sql = ("UPDATE appointment SET deleted=true WHERE id=?"))
public class Appointment extends AbsIntegerEntity {

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Room room;

    @Column(nullable = false)
    private Timestamp appointmentTime;

    @JsonIgnore
    @OneToOne(mappedBy = "appointment")
    private AppointmentResult appointmentResult;

    @JsonIgnore
    @OneToOne(mappedBy = "nextAppointment")
    private AppointmentResult nextAppointmentResult;

}