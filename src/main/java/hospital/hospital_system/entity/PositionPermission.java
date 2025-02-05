package hospital.hospital_system.entity;

import hospital.hospital_system.enums.PermissionEnum;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class PositionPermission extends AbsIntegerEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Position position;

    @Enumerated(EnumType.STRING)
    private PermissionEnum permission;

}
