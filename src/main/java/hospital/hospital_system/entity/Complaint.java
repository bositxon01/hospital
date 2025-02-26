package hospital.hospital_system.entity;

import jakarta.persistence.CascadeType;
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
@SQLDelete(sql = ("UPDATE complaint SET deleted=true WHERE id=?"))
public class Complaint extends AbsIntegerEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL)
    private Patient patient;

}
