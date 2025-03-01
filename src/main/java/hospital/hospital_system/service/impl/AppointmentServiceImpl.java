package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.*;
import hospital.hospital_system.enums.DayEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AppointmentGetDto;
import hospital.hospital_system.payload.AppointmentPostDto;
import hospital.hospital_system.payload.EmployeeAvailableSlotsDto;
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
    public ApiResult<List<AppointmentGetDto>> getAll() {
        List<Appointment> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            return ApiResult.success("No appointments found");
        }
        List<AppointmentGetDto> appointmentGetDTOS = appointments.stream()
                .map(this::getAppointmentDto)
                .toList();
        return ApiResult.success(appointmentGetDTOS);
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

        Optional<Patient> optionalPatient = patientRepository.findById(appointmentPostDto.getPatientId());
        if (optionalPatient.isEmpty()) {
            return ApiResult.error("Patient not found with id " + appointmentPostDto.getPatientId());
        }

        Optional<Employee> optionalEmployee = employeeRepository.findById(appointmentPostDto.getEmployeeId());
        if (optionalEmployee.isEmpty()) {
            return ApiResult.error("Employee not found with id " + appointmentPostDto.getEmployeeId());
        }

        Employee employee = optionalEmployee.get();

        LocalDate appointmentDate = appointmentPostDto.getAppointmentTime().toLocalDate();
        LocalTime appointmentTime = appointmentPostDto.getAppointmentTime().toLocalTime();

        List<EmployeeAvailableSlotsDto> availableSlotsList = getAvailableSlots(appointmentDate);
        Optional<EmployeeAvailableSlotsDto> employeeSlots = availableSlotsList.stream()
                .filter(slot ->
                        slot.getEmployeeId()
                                .equals(employee.getId()))
                .findFirst();

        if (employeeSlots.isEmpty()) {
            return ApiResult.error("Employee is not available on this date: " + appointmentDate);
        }

        boolean isAvailable = employeeSlots.get().getAvailableSlots().contains(appointmentTime.toString());
        if (!isAvailable) {
            return ApiResult.error("Employee is busy at this time: " + appointmentTime);
        }

        Optional<EmployeeRoom> employeeRoomByEmployeeId = employeeRoomRepository.findEmployeeRoomByEmployeeId(employee.getId());
        if (employeeRoomByEmployeeId.isEmpty()) {
            return ApiResult.error("Employee is not available on this date: " + appointmentDate);
        }

        EmployeeRoom employeeRoom = employeeRoomByEmployeeId.get();
        Room room = employeeRoom.getRoom();

        Appointment appointment = new Appointment();
        appointment.setPatient(optionalPatient.get());
        appointment.setEmployee(employee);
        appointment.setRoom(room);
        appointment.setAppointmentTime(Timestamp.valueOf(appointmentPostDto.getAppointmentTime()));

        appointmentRepository.save(appointment);

        return ApiResult.success(getAppointmentDto(appointment));
    }

    @Override
    public List<EmployeeAvailableSlotsDto> getAvailableSlots(LocalDate date) {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeAvailableSlotsDto> result = new ArrayList<>();

        for (Employee employee : employees) {

            DayEnum dayEnum = DayEnum.valueOf(date.getDayOfWeek().name());

            List<WorkTime> workTimes = workTimeRepository.findByEmployeeIdAndDay(employee.getId(), dayEnum);

            Timestamp timestampDate = Timestamp.valueOf(date.atStartOfDay());

            List<Appointment> bookedAppointmentTimes = appointmentRepository.findByEmployeeAndAppointmentTime_Date(employee.getId(), timestampDate);

            for (WorkTime workTime : workTimes) {
                Turn turn = workTime.getTurn();
                List<String> availableSlots = generateAvailableSlots(turn.getStartTime(), turn.getEndTime(), bookedAppointmentTimes);

                if (!availableSlots.isEmpty()) {
                    result.add(new EmployeeAvailableSlotsDto(
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

            slotTime = slotTime.plusMinutes(APPOINTMENT_DURATION);
        }
        return slots;
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
