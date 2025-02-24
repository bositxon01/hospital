package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AppointmentGetDTO;
import hospital.hospital_system.payload.AppointmentPostDTO;
import hospital.hospital_system.payload.EmployeeAvailableSlotsDTO;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    ApiResult<List<AppointmentGetDTO>> getAll();

    ApiResult<AppointmentGetDTO> get(Integer id);

    ApiResult<AppointmentGetDTO> save(AppointmentPostDTO appointmentPostDto);

    List<EmployeeAvailableSlotsDTO> getAvailableSlots(LocalDate date);

}
