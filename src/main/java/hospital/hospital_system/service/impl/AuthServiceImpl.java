package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.User;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.LoginDTO;
import hospital.hospital_system.repository.UserRepository;
import hospital.hospital_system.security.JWTProvider;
import hospital.hospital_system.service.AuthService;
import hospital.hospital_system.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    private final JWTProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    private final Map<String, Long> codeExpiryTimes = new ConcurrentHashMap<>();
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, Boolean> verifiedEmails = new ConcurrentHashMap<>();

    private static final Integer EXPIRY_TIME = 60_000;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public ApiResult<String> login(LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                ));

        User user = (User) authentication.getPrincipal();
        System.out.println("user.getUsername() = " + user.getUsername());

        String token = jwtProvider.generateToken(user);

        System.out.println("token = " + token);

        return ApiResult.success(token);
    }

    @Override
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

    @Override
    public ApiResult<String> verifyResetCode(String email, String code) {
        String storedCode = verificationCodes.get(email);

        if (storedCode == null ||
                System.currentTimeMillis() > codeExpiryTimes.getOrDefault(email, 0L)) {

            verificationCodes.remove(email);
            codeExpiryTimes.remove(email);

            return ApiResult.error("Verification code has expired. Please request a new one.");
        }

        if (!storedCode.equals(code)) {
            return ApiResult.error("Invalid verification code. Please request a new one.");
        }

        verifiedEmails.put(email, true);

        return ApiResult.success("Verification successful. You can now reset your password.");
    }

    @Override
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
