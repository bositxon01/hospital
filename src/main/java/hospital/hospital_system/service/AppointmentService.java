package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AppointmentGetDto;
import hospital.hospital_system.payload.AppointmentPostDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppointmentService {

    ApiResult<List<AppointmentGetDto>> getAll();

    ApiResult<AppointmentGetDto> get(Integer id);

    ApiResult<AppointmentGetDto> save(AppointmentPostDto appointmentPostDto);
}
