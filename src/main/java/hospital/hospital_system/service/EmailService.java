package hospital.hospital_system.service;

public interface EmailService {

    void sendVerificationEmail(String to, String code);

}