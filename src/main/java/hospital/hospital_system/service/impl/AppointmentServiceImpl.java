package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.*;
import hospital.hospital_system.enums.DayEnum;
import hospital.hospital_system.mapper.AppointmentMapper;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AppointmentGetDTO;
import hospital.hospital_system.payload.AppointmentPostDTO;
import hospital.hospital_system.payload.EmployeeAvailableSlotsDTO;
import hospital.hospital_system.repository.*;
import hospital.hospital_system.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final WorkTimeRepository workTimeRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeRoomRepository employeeRoomRepository;
    private final AppointmentMapper appointmentMapper;

    private static final int APPOINTMENT_DURATION = 30;

    @Override
    public ApiResult<List<AppointmentGetDTO>> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findByDeletedFalse();
        if (appointments.isEmpty())
            return ApiResult.error("Appointments not found");

        return ApiResult.success(appointmentMapper.toDTO(appointments));
    }

    @Override
    public ApiResult<AppointmentGetDTO> getAppointmentById(Integer id) {
        return appointmentRepository.findByIdAndDeletedFalse(id)
                .map(appointmentMapper::toDTO)
                .map(ApiResult::success)
                .orElse(ApiResult.error("Appointment not found with id " + id));
    }

    @Override
    public ApiResult<AppointmentGetDTO> createAppointment(AppointmentPostDTO appointmentPostDTO) {
        Optional<Patient> optionalPatient = patientRepository.findByIdAndDeletedFalse(appointmentPostDTO.getPatientId());
        if (optionalPatient.isEmpty())
            return ApiResult.error("Patient not found with id " + appointmentPostDTO.getPatientId());

        Optional<Employee> optionalEmployee = employeeRepository.findByIdAndDeletedFalse(appointmentPostDTO.getEmployeeId());

        if (optionalEmployee.isEmpty())
            return ApiResult.error("Employee not found with id " + appointmentPostDTO.getEmployeeId());

        Patient patient = optionalPatient.get();
        Employee employee = optionalEmployee.get();

        LocalDate appointmentDate = appointmentPostDTO.getAppointmentTime().toLocalDate();
        LocalTime appointmentTime = appointmentPostDTO.getAppointmentTime().toLocalTime();

        boolean isAvailable = getAvailableSlots(appointmentDate).getData().stream()
                .filter(slot -> slot.getEmployeeId().equals(employee.getId()))
                .anyMatch(slot -> slot.getAvailableSlots().contains(appointmentTime.toString()));

        if (!isAvailable)
            return ApiResult.error("Employee is busy at this time: " + appointmentTime);

        Room room = employeeRoomRepository.findEmployeeRoomByEmployeeIdAndDeletedFalse(employee.getId())
                .map(EmployeeRoom::getRoom)
                .orElse(null);

        if (room == null)
            return ApiResult.error("Employee does not have an assigned room");

        Appointment appointment = appointmentMapper.toEntity(appointmentPostDTO);
        appointment.setPatient(patient);
        appointment.setEmployee(employee);
        appointment.setRoom(room);
        appointment.setAppointmentTime(Timestamp.valueOf(appointmentPostDTO.getAppointmentTime()));

        appointmentRepository.save(appointment);

        return ApiResult.success("Appointment created successfully", appointmentMapper.toDTO(appointment));
    }

    @Override
    public ApiResult<List<EmployeeAvailableSlotsDTO>> getAvailableSlots(LocalDate date) {
        List<EmployeeAvailableSlotsDTO> availableSlotsList = new ArrayList<>();

        for (Employee employee : employeeRepository.findByDeletedFalse()) {
            DayEnum dayEnum = DayEnum.valueOf(date.getDayOfWeek().name());
            List<WorkTime> workTimes = workTimeRepository.findByEmployeeIdAndDayAndDeletedFalse(employee.getId(), dayEnum);

            List<Appointment> bookedAppointments = appointmentRepository.findByEmployeeAndAppointmentTime_DateAndDeletedFalse(
                    employee.getId(), Timestamp.valueOf(date.atStartOfDay()));

            List<String> availableSlots = new ArrayList<>();

            for (WorkTime workTime : workTimes) {
                availableSlots.addAll(generateAvailableSlots(
                        workTime.getTurn().getStartTime(),
                        workTime.getTurn().getEndTime(),
                        bookedAppointments
                ));
            }

            if (!availableSlots.isEmpty()) {
                availableSlotsList.add(new EmployeeAvailableSlotsDTO(
                        employee.getId(),
                        employee.getFirstName() + " " + employee.getLastName(),
                        employee.getSpecialization(),
                        availableSlots
                ));
            }
        }

        if (availableSlotsList.isEmpty())
            return ApiResult.error("No available slots found for the given date");

        return ApiResult.success("Available slots fetched successfully", availableSlotsList);
    }

    private List<String> generateAvailableSlots(LocalTime start, LocalTime end, List<Appointment> booked) {
        List<String> slots = new ArrayList<>();

        while (start.isBefore(end)) {
            LocalTime finalStart = start;
            boolean isBooked = booked.stream()
                    .anyMatch(a -> a.getAppointmentTime()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalTime()
                            .equals(finalStart));

            if (!isBooked)
                slots.add(start.toString());

            start = start.plusMinutes(APPOINTMENT_DURATION);
        }

        return slots;
    }

}