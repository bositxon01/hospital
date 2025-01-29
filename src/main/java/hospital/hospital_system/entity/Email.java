package hospital.hospital_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Email cannot be blank")
    @Column(nullable = false, unique = true)
    private String name;

    @ToString.Exclude
    @OneToOne(mappedBy = "email", fetch = FetchType.LAZY)
    private Login login;
}
