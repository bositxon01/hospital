package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.User;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.LoginDTO;
import hospital.hospital_system.repository.UserRepository;
import hospital.hospital_system.service.AuthService;
import hospital.hospital_system.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, Long> codeExpiryTimes = new ConcurrentHashMap<>();
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, Boolean> verifiedEmails = new ConcurrentHashMap<>();

    private static final Integer EXPIRY_TIME = 60_000;

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

    public ApiResult<String> forgetPassword(String email) {
        Optional<User> optionalUser = userRepository.findByUsername(email);

        if (optionalUser.isEmpty()) {
            return ApiResult.error("User not found with email: " + email);
        }

        String verificationCode = generateVerificationCode();

        verificationCodes.put(email, verificationCode);
        codeExpiryTimes.put(email, System.currentTimeMillis() + EXPIRY_TIME);

        emailService.sendVerificationEmail(email, verificationCode);

        return ApiResult.success("Verification code sent successfully to " + email);
    }

    public ApiResult<String> verifyResetCode(String email, String code) {
        String storedCode = verificationCodes.get(email);

        if (storedCode == null || System.currentTimeMillis() > codeExpiryTimes.getOrDefault(email, 0L)) {
            verificationCodes.remove(email);
            return ApiResult.error("Verification code has expired. Please request a new one.");
        }

        if (!storedCode.equals(code)) {
            return ApiResult.error("Invalid verification code. Please request a new one.");
        }

        verifiedEmails.put(email, true);

        return ApiResult.success("Verification successful. You can now reset your password.");
    }

    @Transactional
    public ApiResult<String> resetPassword(String email, String newPassword) {
        Optional<User> optionalUser = userRepository.findByUsername(email);

        if (optionalUser.isEmpty()) {
            return ApiResult.error("User not found with email: " + email);
        }

        if (!verifiedEmails.getOrDefault(email, false)) {
            return ApiResult.error("You must verify your email before resetting your password!");
        }

        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        verificationCodes.remove(email);
        codeExpiryTimes.remove(email);
        verifiedEmails.remove(email);

        return ApiResult.success("Password reset successfully");
    }

    private static String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(100_000, 999_999));
    }
}
