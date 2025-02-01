package hospital.hospital_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(nullable = false)
    private Timestamp appointmentTime;

    @JsonIgnore
    @OneToOne(mappedBy = "appointment")
    private AppointmentResult appointmentResult;

    @JsonIgnore
    @OneToOne(mappedBy = "nextAppointment")
    private AppointmentResult nextAppointmentResult;

}
