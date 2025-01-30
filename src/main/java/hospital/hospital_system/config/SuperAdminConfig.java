package hospital.hospital_system.config;

import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.Position;
import hospital.hospital_system.entity.PositionPermission;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.repository.EmployeeRepository;
import hospital.hospital_system.repository.PositionPermissionRepository;
import hospital.hospital_system.repository.PositionRepository;
import hospital.hospital_system.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
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
        if (userRepository.findByUsername("SUPERADMIN").isEmpty()) {
            user.setUsername("SUPERADMIN@gmail.com");
            user.setPassword(passwordEncoder.encode("SUPERPASSWORD"));
            user.setPosition(position);
            userRepository.save(user);
            System.out.println("SUPERADMIN ADDED");
        }

        Date birthDate = Date.valueOf(LocalDate.of(1990, 1, 1));

        Employee employee = new Employee();
        employee.setFirstName("SUPERADMIN");
        employee.setLastName("SUPERADMIN");
        employee.setDateOfBirth(birthDate);
        employee.setUser(user);
        employeeRepository.save(employee);
    }
}
