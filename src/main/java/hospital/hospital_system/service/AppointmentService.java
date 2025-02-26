package hospital.hospital_system.service;

import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AppointmentGetDTO;
import hospital.hospital_system.payload.AppointmentPostDTO;
import hospital.hospital_system.payload.EmployeeAvailableSlotsDTO;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    ApiResult<List<AppointmentGetDTO>> getAllAppointments();

    ApiResult<AppointmentGetDTO> getAppointmentById(Integer id);

    ApiResult<AppointmentGetDTO> createAppointment(AppointmentPostDTO appointmentPostDto);

    List<EmployeeAvailableSlotsDTO> getAvailableSlots(LocalDate date);

}
