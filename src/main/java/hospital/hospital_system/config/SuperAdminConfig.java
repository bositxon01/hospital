package hospital.hospital_system.config;

import hospital.hospital_system.entity.*;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.enums.RoomEnum;
import hospital.hospital_system.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SuperAdminConfig {
    private final PositionRepository positionRepository;

    private final PositionPermissionRepository positionPermissionRepository;

    private final UserRepository userRepository;

    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoomRepository roomRepository;

    @PostConstruct
    public void init() {
        Position position = positionRepository.findPositionByName("SUPERADMIN")
                .orElseGet(
                        () -> {
                            Position onePosition = new Position();
                            onePosition.setName("SUPERADMIN");
                            onePosition.setSalary(10000.9);
                            positionRepository.save(onePosition);

                            PermissionEnum[] values = PermissionEnum.values();
                            List<PositionPermission> positionPermissionList = new ArrayList<>();

                            for (PermissionEnum value : values) {
                                PositionPermission positionPermission = new PositionPermission();
                                positionPermission.setPermission(value);
                                positionPermission.setPosition(onePosition);
                                positionPermissionRepository.save(positionPermission);
                                positionPermissionList.add(positionPermission);
                            }
                            positionPermissionRepository.saveAll(positionPermissionList);

                            return positionRepository.save(onePosition);
                        }
                );

        User user = new User();
        if (userRepository.findByUsername("SUPERADMIN@gmail.com").isEmpty()) {
            user.setUsername("SUPERADMIN@gmail.com");
            user.setPassword(passwordEncoder.encode("SUPERPASSWORD"));
            user.setPosition(position);
            userRepository.save(user);
            System.out.println("SUPERADMIN ADDED");

            LocalDate birthDate = LocalDate.of(1990, 1, 1);

            Employee employee = new Employee();
            employee.setFirstName("SUPERADMIN");
            employee.setLastName("SUPERADMIN");
            employee.setDateOfBirth(birthDate);
            employee.setSpecialization("SUPERADMIN");
            employee.setUser(user);
            employeeRepository.save(employee);
            roomInit();
        }
    }

    private void roomInit() {
        for (RoomEnum value : RoomEnum.values()) {
            Room room = new Room();
            room.setName(value);
            roomRepository.save(room);
        }
    }
}
