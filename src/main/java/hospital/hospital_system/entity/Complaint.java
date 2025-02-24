package hospital.hospital_system.entity;

import jakarta.persistence.CascadeType;
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
public class Complaint extends AbsIntegerEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL)
    private Patient patient;

}
