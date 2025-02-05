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
    @ToString.Exclude
    private Complaint complaint;

    @ManyToOne
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "patient")
    @ToString.Exclude
    private List<Appointment> appointments;

}
