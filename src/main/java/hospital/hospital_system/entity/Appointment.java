package hospital.hospital_system.entity;

import hospital.hospital_system.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private Patient patient;

    @FutureOrPresent(message = "Appointment time must be in the present or future")
    private Timestamp appointmentTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}
