package hospital.hospital_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity

@SQLRestriction(value = "deleted=false")
@SQLDelete(sql = ("UPDATE appointment_result SET deleted=true WHERE id=?"))
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