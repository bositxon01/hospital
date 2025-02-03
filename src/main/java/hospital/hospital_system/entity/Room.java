package hospital.hospital_system.entity;

import hospital.hospital_system.enums.RoomEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Room extends AbsIntegerEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomEnum name;

    @Value(value = "false")
    private boolean status;

    @OneToOne(mappedBy = "room")
    private Appointment appointment;

    @OneToMany(mappedBy = "room")
    @ToString.Exclude
    private List<EmployeeRoom> employeeRooms;
}
