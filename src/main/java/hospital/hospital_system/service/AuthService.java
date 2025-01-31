package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.LoginDTO;
import hospital.hospital_system.payload.SignUpDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ApiResult<LoginDTO> login(LoginDTO loginDTO);
    ApiResult<SignUpDTO> signUp(SignUpDTO signUpDTO);
}
