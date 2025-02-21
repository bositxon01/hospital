package hospital.hospital_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
public class Appointment extends AbsIntegerEntity {

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Patient patient;

    @OneToOne
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
