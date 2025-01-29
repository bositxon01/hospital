package hospital.hospital_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
