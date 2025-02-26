package hospital.hospital_system.entity;

import hospital.hospital_system.enums.PermissionEnum;
import jakarta.persistence.*;
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
@SQLDelete(sql = ("UPDATE position_permission SET deleted=true WHERE id=?"))
public class PositionPermission extends AbsIntegerEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Position position;

    @Enumerated(EnumType.STRING)
    private PermissionEnum permission;

}
