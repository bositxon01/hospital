package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.*;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AppointmentGetDto;
import hospital.hospital_system.payload.AppointmentPostDto;
import hospital.hospital_system.repository.AppointmentRepository;
import hospital.hospital_system.repository.EmployeeRepository;
import hospital.hospital_system.repository.PatientRepository;
import hospital.hospital_system.service.AppointmentService;
import hospital.hospital_system.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;


    @Override
    public ApiResult<List<AppointmentGetDto>> getAll() {
        List<Appointment> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            return ApiResult.success("No appointments found");
        }
        List<AppointmentGetDto> appointmentGetDtos = appointments.stream()
                .map(this::getAppointmentDto)
                .toList();
        return ApiResult.success(appointmentGetDtos);
    }

    @Override
    public ApiResult<AppointmentGetDto> get(Integer id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isEmpty()) {
            return ApiResult.error("Appointment not found with id " + id);
        }
        Appointment appointment = optionalAppointment.get();
        AppointmentGetDto appointmentDto = getAppointmentDto(appointment);
        return ApiResult.success(appointmentDto);
    }

    @Override
    public ApiResult<AppointmentGetDto> save(AppointmentPostDto appointmentPostDto) {
        List<Appointment> allByAppointmentTime = appointmentRepository.findAllByAppointmentTime(appointmentPostDto.getAppointmentTime());
        for (Appointment appointment : allByAppointmentTime) {
            if (Objects.equals(appointment.getEmployee().getId(), appointmentPostDto.getEmployeeId())) {
                return ApiResult.error("Employee is busy for this time");
            }
        }
        Optional<Patient> optionalPatient = patientRepository.findById(appointmentPostDto.getPatientId());
        if (optionalPatient.isEmpty()) {
            return ApiResult.error("Patient not found with id " + appointmentPostDto.getPatientId());
        }
        Optional<Employee> optionalEmployee = employeeRepository.findById(appointmentPostDto.getEmployeeId());
        if (optionalEmployee.isEmpty()) {
            return ApiResult.error("Employee not found with id " + appointmentPostDto.getEmployeeId());
        }
        List<WorkTime> workTimes = optionalEmployee.get().getWorkTimes();
        return ApiResult.error("Hali hich narsa ishlamayapti");
    }

    private AppointmentGetDto getAppointmentDto(Appointment appointment) {
        AppointmentGetDto appointmentGetDto = new AppointmentGetDto();
        appointmentGetDto.setId(appointment.getId());
        appointmentGetDto.setPatientId(appointment.getPatient().getId());
        appointmentGetDto.setEmployeeId(appointment.getEmployee().getId());
        appointmentGetDto.setRoomId(appointment.getRoom().getId());
        appointmentGetDto.setAppointmentTime(appointment.getAppointmentTime());
        AppointmentResult appointmentResult = appointment.getAppointmentResult();
        if (appointmentResult != null) {
            appointmentGetDto.setAppointmentResultId(appointmentResult.getId());
        }
        return appointmentGetDto;
    }
}
