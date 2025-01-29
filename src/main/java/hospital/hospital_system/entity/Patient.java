package hospital.hospital_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Patient extends Person {
    @OneToOne
    private Complaint complaint;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;
}
