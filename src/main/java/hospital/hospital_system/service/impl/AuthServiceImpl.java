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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public ApiResult<String> login(LoginDTO loginDTO, HttpServletRequest request) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ApiResult.error("User not found with username: " + username);
        }
        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            return ApiResult.error("Wrong password");
        }

        SecurityContext context = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        context.setAuthentication(usernamePasswordAuthenticationToken);
        HttpSession session = request.getSession();
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);

        return ApiResult.success("Login successful");
    }
}
