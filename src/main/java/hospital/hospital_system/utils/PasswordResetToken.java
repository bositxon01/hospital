package hospital.hospital_system.utils;

public record PasswordResetToken(String code, long expiryTime) {
}
