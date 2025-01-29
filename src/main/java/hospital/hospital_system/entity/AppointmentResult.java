package hospital.hospital_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class AppointmentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Appointment appointment;

    @NotBlank
    @Column(nullable = false)
    private String diagnosis;

    private String medicine;

    @OneToOne
    private Appointment nextAppointment;
}
