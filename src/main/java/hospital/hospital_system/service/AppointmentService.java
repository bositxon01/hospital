package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AppointmentGetDto;
import hospital.hospital_system.payload.AppointmentPostDto;
import hospital.hospital_system.payload.EmployeeAvailableSlotsDto;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    ApiResult<List<AppointmentGetDto>> getAll();

    ApiResult<AppointmentGetDto> get(Integer id);

    ApiResult<AppointmentGetDto> save(AppointmentPostDto appointmentPostDto);

    List<EmployeeAvailableSlotsDto> getAvailableSlots(LocalDate date);

}
