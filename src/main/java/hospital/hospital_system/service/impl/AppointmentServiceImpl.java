package hospital.hospital_system.service.impl;

import hospital.hospital_system.entity.*;
import hospital.hospital_system.enums.DayEnum;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.AppointmentGetDto;
import hospital.hospital_system.payload.AppointmentPostDto;
import hospital.hospital_system.payload.DoctorAvailableSlotsDto;
import hospital.hospital_system.repository.AppointmentRepository;
import hospital.hospital_system.repository.EmployeeRepository;
import hospital.hospital_system.repository.PatientRepository;
import hospital.hospital_system.repository.WorkTimeRepository;
import hospital.hospital_system.service.AppointmentService;
import hospital.hospital_system.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final WorkTimeRepository workTimeRepository;
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

    @Override
    public List<DoctorAvailableSlotsDto> getAvailableSlots(LocalDate date) {
        List<Employee> employees = employeeRepository.findAll();
        List<DoctorAvailableSlotsDto> result = new ArrayList<>();

        for (Employee employee : employees) {

            DayEnum dayEnum = DayEnum.valueOf(date.getDayOfWeek().name());

            List<WorkTime> workTimes = workTimeRepository.findByEmployeeIdAndDay(employee.getId(), dayEnum);

            Timestamp timestampDate = Timestamp.valueOf(date.atStartOfDay());

            List<Appointment> bookedAppointmentTimes = appointmentRepository.findByEmployeeAndAppointmentTime_Date(employee.getId(), timestampDate);

            for (WorkTime workTime : workTimes) {
                Turn turn = workTime.getTurn();
                List<String> availableSlots = generateAvailableSlots(turn.getStartTime(), turn.getEndTime(), bookedAppointmentTimes);

                if (!availableSlots.isEmpty()) {
                    result.add(new DoctorAvailableSlotsDto(
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
            slotTime = slotTime.plusMinutes(30); // Each slot is 30 minutes
        }
        return slots;
    }

    @Transactional
    public void bookAppointment(AppointmentPostDto request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Integer patientId = request.getPatientId();
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        if (optionalPatient.isEmpty()) {
            return;
        }
        Patient patient = optionalPatient.get();

        List<EmployeeRoom> employeeRooms = employee.getEmployeeRooms();
        EmployeeRoom employeeRoom = employeeRooms.get(0);
        Room room = employeeRoom.getRoom();

        Appointment appointment = new Appointment();
        appointment.setEmployee(employee);
        appointment.setPatient(patient);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setRoom(room);

        appointmentRepository.save(appointment);
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
