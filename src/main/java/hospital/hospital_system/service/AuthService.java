package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.LoginDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    ApiResult<String> login(LoginDTO loginDTO, HttpServletRequest request);

    ApiResult<String> forgetPassword(String email);

    ApiResult<String> verifyResetCode(String email, String code);

    ApiResult<String> resetPassword(String email, String newPassword);

}
