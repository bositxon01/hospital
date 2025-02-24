package hospital.hospital_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class AppointmentResult extends AbsIntegerEntity {

    @OneToOne
    private Appointment appointment;

    @NotBlank
    @Column(nullable = false, columnDefinition = "text")
    private String diagnosis;

    @Column(columnDefinition = "text")
    private String medicine;

    @OneToOne
    private Appointment nextAppointment;
}
