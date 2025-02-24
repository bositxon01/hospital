package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.LoginDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    ApiResult<String> login(LoginDTO loginDTO);

    ApiResult<String> forgetPassword(String email);

    ApiResult<String> verifyResetCode(String email, String code);

    ApiResult<String> resetPassword(String email, String newPassword);
}
