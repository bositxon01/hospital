package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.LoginDTO;
import hospital.hospital_system.payload.SignUpDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ApiResult<String> login(LoginDTO loginDTO, HttpServletRequest request);

    ApiResult<String> signUp(SignUpDTO signUpDTO);
}
