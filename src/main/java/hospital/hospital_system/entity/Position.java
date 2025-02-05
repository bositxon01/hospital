package hospital.hospital_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class Position extends AbsIntegerEntity {
    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    @Positive
    private Double salary;

    @JsonIgnore
    @OneToMany(mappedBy = "position", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<PositionPermission> positionPermissionList;

    @JsonIgnore
    @OneToMany(mappedBy = "position")
    @ToString.Exclude
    private List<User> users;
}
