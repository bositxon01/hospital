package hospital.hospital_system.entity;

import jakarta.persistence.*;
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

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Appointment> appointments;

}
