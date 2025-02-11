package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AppointmentGetDto;
import hospital.hospital_system.payload.AppointmentPostDto;
import hospital.hospital_system.payload.DoctorAvailableSlotsDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface AppointmentService {

    ApiResult<List<AppointmentGetDto>> getAll();

    ApiResult<AppointmentGetDto> get(Integer id);

    ApiResult<AppointmentGetDto> save(AppointmentPostDto appointmentPostDto);

    List<DoctorAvailableSlotsDto> getAvailableSlots(LocalDate date);

    void bookAppointment(AppointmentPostDto requestDto);
}
