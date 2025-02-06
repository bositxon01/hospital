package hospital.hospital_system.controller;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.LoginDTO;
import hospital.hospital_system.service.AuthService;
import hospital.hospital_system.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final EmailService emailService;
    private final AuthService authService;
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    @PostMapping("/login")
    public ResponseEntity<ApiResult<String>> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        ApiResult<String> apiResult = authService.login(loginDTO, request);
        return ResponseEntity.ok(apiResult);
    }
}
