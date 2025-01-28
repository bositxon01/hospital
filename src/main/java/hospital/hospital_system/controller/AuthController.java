package hospital.hospital_system.controller;

import hospital.hospital_system.config.EmailService;
import hospital.hospital_system.entity.Role;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.enums.RoleEnum;
import hospital.hospital_system.repository.RoleRepository;
import hospital.hospital_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    // Verify email endpoint
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String email, @RequestParam String code) {
        String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(code)) {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                user.setVerified(true);
                userRepository.save(user);
                verificationCodes.remove(email);
                return ResponseEntity.ok("User verified successfully.");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code.");
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
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

    // Create Doctor endpoint (only for ADMIN users)
    @PostMapping("/create-doctor")
    public ResponseEntity<?> createDoctor(@RequestParam String username,
                                          @RequestParam String password,
                                          @RequestParam String email) {
        if (!isSuperAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to perform this action.");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists.");
        }

        Role doctorRole = roleRepository.findByRoleEnum(RoleEnum.DOCTOR)
                .orElseThrow(() -> new IllegalStateException("Doctor role not found in the database"));

        User doctor = new User();
        doctor.setUsername(username);
        doctor.setPassword(passwordEncoder.encode(password));
        doctor.setEmail(email);
        doctor.setRole(doctorRole);
        doctor.setVerified(false);
        userRepository.save(doctor);

        // Generate a verification code and send it via email
        String verificationCode = generateVerificationCode();
        verificationCodes.put(email, verificationCode);
        emailService.sendVerificationEmail(email, verificationCode);

        return ResponseEntity.ok("Doctor created successfully. Verification email sent.");
    }

    // Create Admin endpoint (only for existing super admin)
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestParam String username,
                                         @RequestParam String password,
                                         @RequestParam String email) {
        if (!isSuperAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to perform this action.");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists.");
        }

        Role adminRole = roleRepository.findByRoleEnum(RoleEnum.ADMIN)
                .orElseThrow(() -> new IllegalStateException("Admin role not found in the database"));

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setEmail(email);
        admin.setRole(adminRole);
        admin.setVerified(false);
        userRepository.save(admin);

        // Generate a verification code and send it via email
        String verificationCode = generateVerificationCode();
        verificationCodes.put(email, verificationCode);
        emailService.sendVerificationEmail(email, verificationCode);

        return ResponseEntity.ok("Admin created successfully. Verification email sent.");
    }

    // Utility method to check if the logged-in user is an ADMIN
    private boolean isAdmin() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(user -> user.getRole().getRoleEnum() == RoleEnum.ADMIN).orElse(false);
    }

    private boolean isSuperAdmin() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(user -> user.getUsername().equals("superadmin")).orElse(false);
    }

    // Utility method to generate a random verification code
    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000); // 6-digit random code
    }
}
