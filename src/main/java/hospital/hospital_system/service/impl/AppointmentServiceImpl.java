package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.*;
import hospital.hospital_system.enums.DayEnum;
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

    private static final int APPOINTMENT_DURATION = 30;

    @Override
    public ApiResult<List<AppointmentGetDTO>> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            return ApiResult.success("No appointments found");
        }
        List<AppointmentGetDTO> appointmentGetDTOS = appointments.stream()
                .map(this::getAppointmentDto)
                .toList();
        return ApiResult.success(appointmentGetDTOS);
    }

    @Override
    public ApiResult<AppointmentGetDTO> getAppointmentById(Integer id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isEmpty()) {
            return ApiResult.error("Appointment not found with id " + id);
        }
        Appointment appointment = optionalAppointment.get();
        AppointmentGetDTO appointmentDto = getAppointmentDto(appointment);
        return ApiResult.success(appointmentDto);
    }

    @Override
    public ApiResult<AppointmentGetDTO> createAppointment(AppointmentPostDTO appointmentPostDto) {
        // Check if the patient exists
        Optional<Patient> optionalPatient = patientRepository.findById(appointmentPostDto.getPatientId());
        if (optionalPatient.isEmpty()) {
            return ApiResult.error("Patient not found with id " + appointmentPostDto.getPatientId());
        }

        // Check if the employee exists
        Optional<Employee> optionalEmployee = employeeRepository.findById(appointmentPostDto.getEmployeeId());
        if (optionalEmployee.isEmpty()) {
            return ApiResult.error("Employee not found with id " + appointmentPostDto.getEmployeeId());
        }

        Employee employee = optionalEmployee.get();

        // Extract LocalDate and LocalTime from Timestamp
        LocalDate appointmentDate = appointmentPostDto.getAppointmentTime().toLocalDate();
        LocalTime appointmentTime = appointmentPostDto.getAppointmentTime().toLocalTime();

        // Fetch employee's available slots for the given date
        List<EmployeeAvailableSlotsDTO> availableSlotsList = getAvailableSlots(appointmentDate);
        Optional<EmployeeAvailableSlotsDTO> employeeSlots = availableSlotsList.stream()
                .filter(slot -> slot.getEmployeeId().equals(employee.getId()))
                .findFirst();

        // If no slots are found for the employee on that day, return an error
        if (employeeSlots.isEmpty()) {
            return ApiResult.error("Employee is not available on this date: " + appointmentDate);
        }

        // Check if the selected time is in available slots
        boolean isAvailable = employeeSlots.get().getAvailableSlots().contains(appointmentTime.toString());
        if (!isAvailable) {
            return ApiResult.error("Employee is busy at this time: " + appointmentTime);
        }

        //finding room by EmployeeId
        Optional<EmployeeRoom> employeeRoomByEmployeeId = employeeRoomRepository.findEmployeeRoomByEmployeeIdAndDeletedFalse(employee.getId());
        if (employeeRoomByEmployeeId.isEmpty()) {
            return ApiResult.error("Employee is not available on this date: " + appointmentDate);
        }
        EmployeeRoom employeeRoom = employeeRoomByEmployeeId.get();
        Room room = employeeRoom.getRoom();

        // Create and save the appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(optionalPatient.get());
        appointment.setEmployee(employee);
        appointment.setRoom(room);
        appointment.setAppointmentTime(Timestamp.valueOf(appointmentPostDto.getAppointmentTime()));

        appointmentRepository.save(appointment);

        return ApiResult.success(getAppointmentDto(appointment));
    }

    @Override
    public List<EmployeeAvailableSlotsDTO> getAvailableSlots(LocalDate date) {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeAvailableSlotsDTO> result = new ArrayList<>();

        for (Employee employee : employees) {

            DayEnum dayEnum = DayEnum.valueOf(date.getDayOfWeek().name());

            List<WorkTime> workTimes = workTimeRepository.findByEmployeeIdAndDayAndDeletedFalse(employee.getId(), dayEnum);

            Timestamp timestampDate = Timestamp.valueOf(date.atStartOfDay());

            List<Appointment> bookedAppointmentTimes = appointmentRepository.findByEmployeeAndAppointmentTime_DateAndDeletedFalse(employee.getId(), timestampDate);

            for (WorkTime workTime : workTimes) {
                Turn turn = workTime.getTurn();
                List<String> availableSlots = generateAvailableSlots(turn.getStartTime(), turn.getEndTime(), bookedAppointmentTimes);

                if (!availableSlots.isEmpty()) {
                    result.add(new EmployeeAvailableSlotsDTO(
                            employee.getId(),
                            employee.getFirstName() + " " + employee.getLastName(),
                            employee.getSpecialization(),
                            availableSlots
                    ));
                }
            }
        }
        return result;
    }

    private List<String> generateAvailableSlots(LocalTime start, LocalTime end, List<Appointment> booked) {
        List<String> slots = new ArrayList<>();
        LocalTime slotTime = start;

        while (slotTime.isBefore(end)) {
            String formattedTime = slotTime.toString();
            LocalTime finalSlotTime = slotTime;

            boolean isBooked = booked.stream()
                    .anyMatch(a -> a.getAppointmentTime()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalTime()
                            .equals(finalSlotTime));

            if (!isBooked) {
                slots.add(formattedTime);
            }

            slotTime = slotTime.plusMinutes(APPOINTMENT_DURATION); // Each slot is 30 minutes
        }
        return slots;
    }

    private AppointmentGetDTO getAppointmentDto(Appointment appointment) {
        AppointmentGetDTO appointmentGetDto = new AppointmentGetDTO();

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
