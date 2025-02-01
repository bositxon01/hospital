package hospital.hospital_system.controller;

import hospital.hospital_system.aop.CheckAuth;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.LoginDTO;
import hospital.hospital_system.payload.SignUpDTO;
import hospital.hospital_system.repository.UserRepository;
import hospital.hospital_system.service.AuthService;
import hospital.hospital_system.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthService authService;
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        ApiResult<LoginDTO> apiResult = authService.login(loginDTO, request);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/sign-up")
    @CheckAuth(permissions = PermissionEnum.CREATE_EMPLOYEE)
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO) {
        ApiResult<SignUpDTO> apiResult = authService.signUp(signUpDTO);
        return ResponseEntity.ok(apiResult);
    }

}
