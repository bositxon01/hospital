package hospital.hospital_system.controller;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.LoginDTO;
import hospital.hospital_system.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication API", description = "Authentication CRUD API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResult<String>> login(@Valid @RequestBody LoginDTO loginDTO) {
        ApiResult<String> apiResult = authService.login(loginDTO);
        return ResponseEntity.ok(apiResult);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<ApiResult<String>> forgetPassword(@RequestParam String email) {
        ApiResult<String> response = authService.forgetPassword(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<ApiResult<String>> verifyResetCode(@RequestParam String email,
                                                             @RequestParam String code) {
        ApiResult<String> response = authService.verifyResetCode(email, code);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResult<String>> resetPassword(@RequestParam String email,
                                                           @RequestParam String newPassword) {
        ApiResult<String> response = authService.resetPassword(email, newPassword);
        return ResponseEntity.ok(response);
    }
}
