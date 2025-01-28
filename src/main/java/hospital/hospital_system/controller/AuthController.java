package hospital.hospital_system.controller;

import hospital.hospital_system.config.EmailService;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String email,
                                         @RequestParam String code) {
        String storedCode = verificationCodes.get(email);

        if (storedCode != null && storedCode.equals(code)) {
            User user = userRepository.findAll()
                    .stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst()
                    .orElse(null);

            if (user != null) {
                user.setVerified(true);
                userRepository.save(user);
                verificationCodes.remove(email);
                return ResponseEntity.ok("Patient verified successfully. Verification email sent.");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }

            if (!user.isVerified()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Please verify your email before logging in.");
            }
            return ResponseEntity.ok("Login successful!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

}