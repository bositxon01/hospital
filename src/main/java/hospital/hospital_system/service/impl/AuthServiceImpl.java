package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.User;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.LoginDTO;
import hospital.hospital_system.repository.UserRepository;
import hospital.hospital_system.security.JWTProvider;
import hospital.hospital_system.service.AuthService;
import hospital.hospital_system.service.EmailService;
import hospital.hospital_system.utils.PasswordResetToken;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
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

    private final Map<String, PasswordResetToken> resetTokens = new ConcurrentHashMap<>();

    private static final long EXPIRY_TIME = 5 * 60 * 1000;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameAndDeletedFalse(username)
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
        Optional<User> optionalUser = userRepository.findByUsernameAndDeletedFalse(email);

        if (optionalUser.isEmpty())
            return ApiResult.error("User not found with email: " + email);

        String verificationCode = generateVerificationCode();

        long expiryTime = System.currentTimeMillis() + EXPIRY_TIME;

        resetTokens.put(email, new PasswordResetToken(verificationCode, expiryTime));

        emailService.sendVerificationEmail(email, verificationCode);

        return ApiResult.success("Verification code sent successfully to " + email);
    }

    @Override
    @Transactional
    public ApiResult<String> resetPassword(String email, String code, String newPassword) {
        PasswordResetToken resetToken = resetTokens.get(email);

        if (resetToken == null || System.currentTimeMillis() > resetToken.expiryTime()) {
            resetTokens.remove(email);
            return ApiResult.error("Verification code expired. Request a new one.");
        }

        if (!resetToken.code().equals(code))
            return ApiResult.error("Invalid verification code.");

        User user = userRepository.findByUsernameAndDeletedFalse(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetTokens.remove(email);

        return ApiResult.success("Password reset successfully.");
    }

    @Scheduled(fixedRate = 60_000)
    public void cleanupExpiredTokens() {
        long now = System.currentTimeMillis();

        resetTokens.entrySet()
                .removeIf(
                        entry ->
                                now > entry.getValue().expiryTime());
    }

    private static String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(100_000, 999_999));
    }
}