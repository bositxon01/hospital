package hospital.hospital_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Appointment extends AbsIntegerEntity {

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Patient patient;

    @OneToOne
    private Room room;

    @NotBlank
    @Column(nullable = false)
    private Timestamp appointmentTime;

    @OneToOne(mappedBy = "appointment")
    private AppointmentResult appointmentResult;

    @OneToOne(mappedBy = "nextAppointment")
    private AppointmentResult nextappointmentResult;

}
