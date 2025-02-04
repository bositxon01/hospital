package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.HomeDto;
import org.springframework.stereotype.Service;

@Service
public interface HomeService {
    public ApiResult<HomeDto> getHome();
}
