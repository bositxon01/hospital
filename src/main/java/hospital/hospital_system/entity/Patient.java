package hospital.hospital_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@FieldNameConstants

@SQLRestriction(value = "deleted=false")
@SQLDelete(sql = ("UPDATE patient SET deleted=true WHERE id=?"))
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