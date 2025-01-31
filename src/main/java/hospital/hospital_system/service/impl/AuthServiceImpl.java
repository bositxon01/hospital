package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.Position;
import hospital.hospital_system.entity.PositionPermission;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.*;
import hospital.hospital_system.repository.EmployeeRepository;
import hospital.hospital_system.repository.PositionPermissionRepository;
import hospital.hospital_system.repository.PositionRepository;
import hospital.hospital_system.repository.UserRepository;
import hospital.hospital_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PositionRepository positionRepository;
    private final PositionPermissionRepository positionPermissionRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResult<LoginDTO> login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ApiResult.error("User not found with username: " + username);
        }

        if (!passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            return ApiResult.error("Wrong password");
        }

        return ApiResult.success("Login successful");
    }

    @Override
    public ApiResult<SignUpDTO> signUp(SignUpDTO signUpDTO) {
        PositionDTO positionDTO = signUpDTO.getPositionDTO();
        Position position = new Position();
        position.setName(positionDTO.getName());
        position.setSalary(positionDTO.getSalary());

        String positonName = positionDTO.getName();
        Optional<Position> positionByName = positionRepository.findPositionByName(positonName);

        User user = new User();
        if (positionByName.isEmpty()) {
            positionRepository.save(position);
            List<PermissionEnum> permissions = signUpDTO.getPermissions();


            for (PermissionEnum permission : permissions) {
                PositionPermission positionPermission = new PositionPermission();
                positionPermission.setPosition(position);
                positionPermission.setPermission(permission);
                positionPermissionRepository.save(positionPermission);
            }

            user.setUsername(signUpDTO.getUsername());
            user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
            user.setPosition(position);
            userRepository.save(user);
        } else {
            user.setUsername(signUpDTO.getUsername());
            user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
            user.setPosition(positionByName.get());
            userRepository.save(user);
        }

        EmployeeDTO employeeDTO = signUpDTO.getEmployeeDTO();
        Employee employee = new Employee();
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setSpecialization(employeeDTO.getSpecialization());
        employee.setDateOfBirth(employeeDTO.getBirthDate());
        employee.setUser(user);
        employeeRepository.save(employee);

        return ApiResult.success("Sign up successful");
    }
}
