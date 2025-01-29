package hospital.hospital_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    @Positive
    private Double salary;

    @OneToMany(mappedBy = "position")
    private List<PositionPermission> positionPermissionList;

    @OneToMany(mappedBy = "position")
    private List<Employee> employeeList;
}
