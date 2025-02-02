package hospital.hospital_system.entity;

import jakarta.persistence.*;
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
@SQLDelete(sql =( "update complaint set deleted=true where id=?"))
public class Complaint  extends AbsIntegerEntity{

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @OneToOne(mappedBy = "complaint")
    private Patient patient;

}
